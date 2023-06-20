# Easy Tutorial Step 1

## Overview
This extension will be used as starting point for a tutorial on the Easy framework

## Setup
Here are the instruction to configure locally the extension and make it works. The extension has been developed to work with the standard electronics-spa accelerator, although it can work easily with other spa accelerators as well with small changes.

### Install SAP Commerce Platform

First thing is then to install the latest 2205 commerce version. Follow the official documentation:

- [Using the Installer Recipes](https://help.sap.com/docs/SAP_COMMERCE_CLOUD_PUBLIC_CLOUD/8be4e0379b294fbabf36d26e7ca4169d/cb3ff964e4784073a70f06165efbac8a.html?locale=en-US&version=v2205) to install using the cx recipe
- [Installing SAP Commerce Cloud Manually](https://help.sap.com/docs/SAP_COMMERCE_CLOUD_PUBLIC_CLOUD/8be4e0379b294fbabf36d26e7ca4169d/8bf5cfea86691014a00e9705a3c84074.html?locale=en-US&version=v2205) to install manually if you know the required extensions to set up the electronics or apparel-uk accelerator

### Install the Easy Framework
1. Download [Easy Extensibility Framework extensions](https://github.tools.sap/cx-boosters/easy-extensibility-framework)
2. Add Easy core extensions to your localextensions.xml file:
```
<extension dir="easycore" />
<extension dir="easytype" />    
<extension dir="easybackoffice" />    
<extension dir="easyrest" />
```
3. Run `ant clean all`
4. Run `ant initialize -Dtenant=master`
5. Start SAP Commerce Cloud server with `<HYBRIS_HOME>/bin/platform/hybrisserver.sh`

### Configure easy repositories
Next you'll have to configure the git repository where the tutorial extension is stored. If you want to play around locally with the extension we suggest you to create a local easy repository that will simplify and speed up your development.
Follow these steps:
- clone locally the [Easy Extension Sample](https://github.tools.sap/cx-boosters/easy-extension-samples/tree/easy-0.2) repository (branch easy-0.2)
- Login into the backoffice and find the Easy node on the Navigation Tree, open Easy Core, Repositories and click on the + button to create a new "local repository"
![img.png](./images/img.png)
- populate the wizard giving the repo a code, name and the path of your local File System where you've cloned the remote easy-extension-samples repo
![img_1.png](./images/img_1.png)

### Install the easy extensions
Easy extensions have to be installed through the Administration Console:
- connect to the hac and log in
- if you've correctly created your easy local repository, you should see under the Easy tab, the repository with the list of easy extensions retrieved:
![img_9.png](./images/img_9.png)
- Click the "Update" button to be sure you've the latest version of your local repository: the update button will refresh the copy of the repositories that is stored in the _data_ dir of Commerce
- Click on the "+" button next to the "Easy Tutorial Step 1" extension and wait the installation procedure is completed: if there are no errors you should see a successful blue message banner in the hac, if something went wrong during installation, a red message banner will pop up in the hac. In this case you'll have to check the server log to see what is the problem
- We suggest you to repeat the last step also for the "Easy Api" extension that will give you access to a Swagger endpoint so that you can easily test the endpoints of the tutorial extension 



### Set up the extension locally on your IDE

To set up the easy-tutorial-step1 extension in your local IDE you need to:
1. add a file _gradle.properties_ to the root of the extension.
```
easyRepoDir=/Users/I318914/SAPDevelop/projects/Easy/repoEasy/easy-extension-samples/easy-tutorial-step1
easyDeployUrl=http://localhost:9001/easyrest/easy/deploy
systemProp.org.ajoberstar.grgit.auth.username=<my git username>
systemProp.org.ajoberstar.grgit.auth.password=<my personal git token>
groovyHacHybrisHomeDir=/Users/I318914/SAPDevelop/projects/Easy/CXCOMCL221100P_5-70007431/hybris
```
The first four properties are needed if you want to use gradle and the grgit plugin to deploy your extension locally (it's not really needed, you can simply adopt the procedure we've described above)
The last groovyHacHybrisHomeDir property must point to the local installation of your Commerce platform and will be used to set up all dependencies 
from the platform extensions so that your easy extension can be imported in the IDE without issues.
Here's how you can set up IntelliJ IDEA with the extension. First import your Commerce project into IDEA.
2. import your Commerce project into IDEA
3. import a new module from existing sources following these steps:
   1. click on File -> Project Structure -> Module -> + -> Import Module
      ![img_3.png](./images/img_3.png)
   2. in the wizard, select the root folder of the easy-tutorial-step1 extension and then select _Import module from external_ and select _Gradle_ option
   ![img_4.png](./images/img_4.png)
   3. Click _Finish_ to complete the import process. You should be able to correctly see the imported easy extension
   ![img_10.png](./images/img_10.png)

That's it! You should now be able to develop with your IDE and also when needed you can start the Remote debugging and debug the groovy code of easy extension that you installed. 

## Tutorial tasks
Your only task for this step of the tutorial is to follow the above instructions for the setup of your local environment. Once completed, you can execute the sample unite test, simply running _gradle test_ from the root of the project and youl should see also a generated report under the _build/reports_ folder

## Next Step
Once completed, simply uninstall this Easy Tutorial Step 1 extension and install the next one: Easy Tutorial Step 2 extension 