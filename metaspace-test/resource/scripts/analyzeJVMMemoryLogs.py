import os
import re
import csv
import matplotlib.pyplot as plt  # Add this import
import re
from datetime import datetime
import sys

def parse_metaspace_file(file_path):
    loaders = None
    classes = None
    metaspace_committed = None
    reserved_mb = None
    with open(file_path, 'r') as f:
        for line in f:
            # Example: "Total Usage - 3757 loaders, 70910 classes (1354 shared):"
            match_total = re.search(r'Total Usage\s*-\s*(\d+)\s+loaders,\s*(\d+)\s+classes', line)
            if match_total:
                loaders = int(match_total.group(1))
                classes = int(match_total.group(2))
            # Example: "Metaspace non-space committed: 12345678"
            match_nonclass = re.search(
                r'Non-class space:\s*([\d.]+)\s*MB reserved,\s*([\d.]+)\s*MB.*?committed', line)
            if match_nonclass:
                reserved_mb = float(match_nonclass.group(1))
                committed_mb = float(match_nonclass.group(2))
                if committed_mb:
                    metaspace_committed = committed_mb
    return loaders, classes, reserved_mb, metaspace_committed

def fetch_metaspace_data(base_folder,input_dir):
    results = []
    output_folder = os.path.join(base_folder, 'output', input_dir)
    if not os.path.exists(output_folder):
        print(f"Folder not found: {output_folder}")
        return results

    for timestamp_folder in sorted(os.listdir(output_folder)):
        folder_path = os.path.join(output_folder, timestamp_folder)
        metaspace_file = os.path.join(folder_path, 'metaspace.txt')
        if os.path.isfile(metaspace_file):
            loaders, classes, metaspace_reserverd, metaspace_committed = parse_metaspace_file(metaspace_file)
            results.append({
                'timestamp': timestamp_folder,
                'loaders': loaders,
                'classes': classes,
                'metaspace_non_class_space_reserved': metaspace_reserverd,
                'metaspace_non_class_space_committed': metaspace_committed
            })
            
            
            # Calculate growth differences for metaspace_non_class_space_committed and classes
            prev_committed = None
            prev_classes = None
            for entry in results:
                committed = entry['metaspace_non_class_space_committed']
                classes = entry['classes']
                if prev_committed is not None and committed is not None:
                    entry['committed_growth'] = round(committed - prev_committed, 2)
                else:
                    entry['committed_growth'] = None
                if prev_classes is not None and classes is not None:
                    entry['classes_growth'] = classes - prev_classes
                else:
                    entry['classes_growth'] = None
                prev_committed = committed
                prev_classes = classes
            
     
    # Add install/uninstall counts to each result entry
    log_file = os.path.join(base_folder, "easy_extension_installation.log")
    prev_timestamp = None
    for idx, entry in enumerate(results):
        timestamp = entry['timestamp']
        if idx == 0:
            # Skip first row
            entry['install_count'] = None
            entry['uninstall_count'] = None
            prev_timestamp = timestamp
            continue
        try:
            # Assume each timestamp is in format 'YYYYMMDD_HHMMSS'
            start_dt = datetime.strptime(prev_timestamp, "%Y%m%d_%H%M%S")
            end_dt = datetime.strptime(timestamp, "%Y%m%d_%H%M%S")
            start_time = start_dt.strftime("%Y-%m-%d %H:%M:%S,000")
            end_time = end_dt.strftime("%Y-%m-%d %H:%M:%S,999")
            installs, uninstalls = count_install_uninstall_operations(log_file, start_time, end_time)
            entry['install_count'] = installs
            entry['uninstall_count'] = uninstalls
        except Exception:
            entry['install_count'] = None
            entry['uninstall_count'] = None
        prev_timestamp = timestamp
            
            
    return results



def count_install_uninstall_operations(
    log_file_path,
    start_time_str,
    end_time_str,
    time_format="%Y-%m-%d %H:%M:%S,%f"
):
    """
    Counts the number of install and uninstall operations in the log file
    between start_time and end_time.

    Args:
        log_file_path (str): Path to the log file.
        start_time_str (str): Start time in string format.
        end_time_str (str): End time in string format.
        time_format (str): Datetime format used in the log.

    Returns:
        (int, int): Tuple of (install_count, uninstall_count)
    """
    install_msg = "Installation request for repository 'easy-extensions' and extension 'metaspacetest' processed."
    uninstall_msg = "Uninstallation request for repository 'easy-extensions' and extension 'metaspacetest' processed."

    start_time = datetime.strptime(start_time_str, time_format)
    end_time = datetime.strptime(end_time_str, time_format)

    install_count = 0
    uninstall_count = 0

    # Regex to extract timestamp from each log line
    timestamp_regex = re.compile(r'^(\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2},\d{3})')

    with open(log_file_path, 'r') as f:
        for line in f:
            match = timestamp_regex.match(line)
            if match:
                log_time = datetime.strptime(match.group(1), time_format)
                if start_time <= log_time <= end_time:
                    if install_msg in line:
                        install_count += 1
                    elif uninstall_msg in line:
                        uninstall_count += 1

    return install_count, uninstall_count

def plot_analysis_graphs(data, output_dir):
    if not os.path.exists(output_dir):
        os.makedirs(output_dir)

    # Take every 3rd data point to get 15 min frequency (since original is 5 min)
    data_15min = data[::1]

    timestamps = [entry['timestamp'] for entry in data_15min]
    reserved = [entry['metaspace_non_class_space_reserved'] for entry in data_15min]
    committed = [entry['metaspace_non_class_space_committed'] for entry in data_15min]
    loaders = [entry['loaders'] for entry in data_15min]
    classes = [entry['classes'] for entry in data_15min]

    # 1. Metaspace reserved and committed
    plt.figure(figsize=(10, 6))
    plt.plot(timestamps, reserved, label='Reserved (MB)', marker='o')
    plt.plot(timestamps, committed, label='Committed (MB)', marker='o')
    plt.xlabel('Timestamp (Frequency - 5 minutes)')
    plt.ylabel('MB')
    plt.title('Metaspace Reserved vs Committed Over Time')
    plt.legend()
    plt.xticks(rotation=45)
    plt.tight_layout()
    plt.savefig(os.path.join(output_dir, 'metaspace_reserved_committed.png'))
    plt.close()

    # 2. Loaders
    plt.figure(figsize=(10, 6))
    plt.plot(timestamps, loaders, label='Loaders', marker='o', color='orange')
    plt.xlabel('Timestamp (Frequency - 5 minutes)')
    plt.ylabel('Metaspace Class Loaders Count')
    plt.title('Metaspace Class Loaders Count Over Time')
    plt.xticks(rotation=45)
    plt.tight_layout()
    plt.savefig(os.path.join(output_dir, 'loaders.png'))
    plt.close()

    # 3. Classes
    plt.figure(figsize=(10, 6))
    plt.plot(timestamps, classes, label='Classes', marker='o', color='green')
    plt.xlabel('Timestamp (Frequency - 5 minutes)')
    plt.ylabel('Metaspace Classes Count')
    plt.title('Metaspace Classes Count Over Time')
    plt.xticks(rotation=45)
    plt.tight_layout()
    plt.savefig(os.path.join(output_dir, 'classes.png'))
    plt.close()



# Example usage:
if __name__ == "__main__":
    
    if len(sys.argv) < 2:
        print("Usage: python analysisLog.py <directory_name>")
        sys.exit(1)

    input_dir = sys.argv[1]

    base_folder = os.path.dirname(os.path.abspath(__file__))
    data = fetch_metaspace_data(base_folder,input_dir)
    for entry in data:
        print(entry)
    # Save graphs in 'output/graph'
    output_graph_dir = os.path.join(base_folder, 'output',input_dir,'graph')
    plot_analysis_graphs(data, output_graph_dir)
    
    csv_output_path = os.path.join(base_folder, 'output', input_dir, 'metaspace_analysis.csv')
    with open(csv_output_path, 'w', newline='') as csvfile:
        fieldnames = [
            'timestamp',
            'loaders',
            'classes',
            'metaspace_non_class_space_reserved',
            'metaspace_non_class_space_committed',
            'committed_growth',
            'classes_growth',
            'install_count',
            'uninstall_count'
        ]
        writer = csv.DictWriter(csvfile, fieldnames=fieldnames)
        writer.writeheader()
        for entry in data:
            writer.writerow(entry)
    print(f"CSV data saved to {csv_output_path}")
    
    
    log_file = "easy_extension_installation.log"
    start = "2025-06-16 18:05:43,599"
    end   = "2025-06-16 19:05:44,043"

    installs, uninstalls = count_install_uninstall_operations(log_file, start, end)
    print(f"Installs: {installs}, Uninstalls: {uninstalls}")