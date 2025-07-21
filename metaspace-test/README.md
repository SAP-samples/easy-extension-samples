The **Metaspace Test** extension is derived from the **Alternate Currency Price** extension. Its primary purpose is to evaluate the impact of large Easy Extensions on JVM Metaspace usage.

This extension introduces an additional 1000 DTOs, which are dynamically loaded via `easyBeans.groovy`. By significantly increasing the number of DTOs, the extension provides a realistic scenario for analyzing class loading and Metaspace consumption during extension lifecycle operations.

# Metaspace Test Easy Extension Features

This extension is based on the Alternate Currency Price extension. For more technical and functional details, refer to the Alternate Currency Price documentation:  
https://github.com/SAP-samples/easy-extension-samples/tree/main/alternatecurrencyprice

## Additional Changes

1000 DTOs are generated in the DTO folder using `dtoGenerator.groovy` and loaded through `EasyBeans.groovy`.

# JVM Memory Impact Analysis Scripts

This extension provides a set of scripts to help you measure and analyze the impact of large Easy Extensions on JVM memory, with a particular focus on Metaspace usage. These scripts automate the collection of JVM diagnostics, simulate extension lifecycle operations, and generate detailed reports to help you assess the memory footprint of your extension.

These scripts have been tested and validated on a local development environment. All memory analysis described here was performed on localhost.

To perform a complete analysis, use the following scripts together:
- `jvmLogs.sh`: Collects periodic JVM memory and Metaspace diagnostics.
- `easyExtensionOperation.py`: Automates install/uninstall cycles of the Easy Extension.
- `analyzeJVMMemoryLogs.py`: Analyzes collected logs and generates summary reports.

See the sections below for detailed instructions on each script and the overall analysis workflow.


## jvmLogs.sh: JVM Diagnostics Collection Script

This script automates the periodic collection of JVM diagnostics—including Metaspace usage, Heap information, Class Hierarchy, and Classloader details—from a running Java process using `jcmd`. Each run saves the output in a timestamped subdirectory for organized analysis.

### Usage

```bash
sh jvmLogs.sh <jvm_pid> <output_folder>
```

- `<jvm_pid>`: The process ID of the JVM to monitor (find with `jcmd` or `jps`).
- `<output_folder>`: Directory where logs will be saved. The script creates timestamped subfolders for each interval.

### Example

```bash
sh jvmLogs.sh 49744 output/localhost-easy-1
```

This command logs diagnostics every 5 minutes, saving the following files in a new timestamped folder under `./output/localhost-easy-1`:

- `metaspace.txt`
- `heapinfo.txt`
- `class_hierarchy.txt`
- `classloaders.txt`

### Notes

- The script runs continuously until manually stopped (e.g., with `Ctrl+C`).
- Requires `jcmd` (included with the JDK) and appropriate permissions to access the JVM process.
- Useful for tracking JVM Metaspace and classloader behavior during extension lifecycle operations.

## easyExtensionOperation.py: Easy Extension Install/Uninstall

This Python script automates the installation and uninstallation of the "easy extension" for SAP Commerce via REST API calls. It can be used for single operations or to repeatedly install/uninstall in a loop for stress or lifecycle testing.

### Features
- Installs or uninstalls the extension using HTTP POST requests.
- Logs all actions and responses to `easy_extension_installation.log`.
- Can run in a loop for a specified duration (default: 120 minutes if no action is specified).

### Requirements
- Python 3.x
- `requests` and `urllib3` libraries (install via `pip install requests urllib3`)

### Usage
```bash
python easyExtensionOperation.py [install|uninstall]
```
- `install`: Installs the extension.
- `uninstall`: Uninstalls the extension.
- If no argument is provided, the script will loop install/uninstall actions for 120 minutes (default).

#### Example: Install Extension
```bash
python easyExtensionOperation.py install
```

#### Example: Uninstall Extension
```bash
python easyExtensionOperation.py uninstall
```

#### Example: Loop Install/Uninstall for 2 Hours
```bash
python easyExtensionOperation.py
```

### Configuration
- Update `HOSTNAME` and `API_KEY` in the script as needed for your environment.
- The script disables SSL verification for local testing. For production, enable SSL verification.

### Log Output
- All actions and API responses are logged to `easy_extension_installation.log` in the script directory.

## analyzeJVMMemoryLogs.py: Summary and Usage

This Python script analyzes JVM memory logs collected by the `jvmLogs.sh` script. It parses `metaspace.txt` files from timestamped folders, correlates them with install/uninstall events from the extension log, and generates CSV reports and graphs for JVM Metaspace usage trends.

### Features
- Parses JVM Metaspace logs for class loader and class counts, reserved and committed memory.
- Calculates growth between intervals for memory and class counts.
- Correlates memory data with install/uninstall events from `easy_extension_installation.log`.
- Outputs a CSV file with all metrics.
- Generates graphs (PNG) for:
  - Metaspace reserved vs committed
  - Class loader count
  - Class count

### Requirements
- Python 3.x
- `matplotlib` (install via `pip install matplotlib`)

### Usage
```bash
python analyzeJVMMemoryLogs.py <directory_name>
```
- `<directory_name>`: The subfolder under `output/` containing the timestamped JVM log folders (created by `jvmLogs.sh`).

#### Example
```bash
python analyzeJVMMemoryLogs.py localhost-easy-1
```

### Output
- CSV file: `output/<directory_name>/metaspace_analysis.csv`
- Graphs: `output/<directory_name>/graph/`
- Console output: Prints parsed data and install/uninstall counts for each interval.

### Notes
- Make sure the log and output folders exist and contain the expected files.
- Update the script if your log format or folder structure differs.



## Steps for JVM Metaspace Analysis

1. **Start JVM Memory Logging**  
    Run `jvmLogs.sh` to begin capturing JVM memory logs every 5 minutes. This will create timestamped folders with diagnostic data.

2. **Run Extension Lifecycle Operations**  
    Execute `easyExtensionOperation.py` to automate the install and uninstall of the Easy Extension. This simulates extension lifecycle events and logs each operation.

3. **Analyze Collected Data**  
    After the extension operations are complete, run `analyzeJVMMemoryLogs.py`. This script processes the collected logs and generates a summary report with memory usage trends and event correlations.



