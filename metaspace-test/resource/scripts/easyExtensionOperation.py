import requests
import argparse
import time
import urllib3
import logging

# Configure logging
logging.basicConfig(
    filename='./easy_extension_installation.log',
    level=logging.INFO,
    format='%(asctime)s %(levelname)s: %(message)s'
)

# Placeholders for configuration
HOSTNAME = "https://localhost:9002"
API_KEY = "ODc5NjA5MzEwODc0N1swLCA0MSwgLTg0LCAtNTgsIC05MCwgLTgwLCAyNiwgMjQsIDQ2LCAtMzNd"

# Endpoints
INSTALL_ENDPOINT = f"{HOSTNAME}/easyrest/easyapi/repository/easyextensions-git/extension/metaspacetest/install?async=false"
UNINSTALL_ENDPOINT = f"{HOSTNAME}/easyrest/easyapi/repository/easyextensions-git/extension/metaspacetest/uninstall"

HEADERS = {
    "x-api-key": API_KEY,
    "Content-Type": "application/json",
    "Accept": "application/json"
}

def install_extension():
    payload = {
        # Add your required JSON payload fields here
        
    }
    try:
        urllib3.disable_warnings(urllib3.exceptions.InsecureRequestWarning)
        response = requests.post(INSTALL_ENDPOINT, headers=HEADERS, json=payload, verify=False)
        logging.info(f"Install status: {response.status_code}")
        logging.info(response.text)
    except Exception as e:
        logging.error(f"Error installing extension: {e}")

def uninstall_extension():
    try:
        urllib3.disable_warnings(urllib3.exceptions.InsecureRequestWarning)
        response = requests.post(UNINSTALL_ENDPOINT, headers=HEADERS, verify=False)
        logging.info(f"Uninstall status: {response.status_code}")
        logging.info(response.text)
    except Exception as e:
        logging.error(f"Error uninstalling extension: {e}")

def loop_actions(minutes):
    end_time = time.time() + minutes * 60
    count = 0
    while time.time() < end_time:
        count += 1
        logging.info(f"--- Loop {count} ---")
        logging.info("Installing extension...")
        install_extension()
        logging.info("Uninstalling extension...")
        uninstall_extension()
        time.sleep(10)  # Optional: add a small delay to avoid hammering the server

if __name__ == "__main__":

    parser = argparse.ArgumentParser(description="Install or uninstall easy extension on SAP Commerce.")
    parser.add_argument("action", nargs="?", choices=["install", "uninstall"], help="Action to perform: install or uninstall")
    args = parser.parse_args()
    if args.action == "install":
        install_extension()
    elif args.action == "uninstall":
        uninstall_extension()
    else:
        logging.info("No action specified. Use 'install' or 'uninstall'.")
        loop_actions(120)
