# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.4.3/gradle-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.4.3/gradle-plugin/reference/html/#build-image)  
* [Code Conventions java](https://www.oracle.com/java/technologies/javase/codeconventions-namingconventions.html)
  
### Pre requirements   
* [Download and install eclipse](https://www.eclipse.org/downloads/packages/release/helios/sr1/eclipse-ide-java-developers)
* [Download loombok](https://projectlombok.org/download)  
* [Install loombok](https://openwebinars.net/blog/como-instalar-y-usar-lombok)
* [Install JDK8 Java](https://www.oracle.com/co/java/technologies/javase/javase-jdk8-downloads.html) 
 
### Build Project
Follow these steps to build your project:

* Go to the root of the project and right click, once the window shows, follow the sequence: Build Path> Configure Build Path (click) follow in the drop down window, click on the Gradle tab and select the field that says override workspace settings and select gradle version 6.4.1 and finish by clicking on the application and then on apply and close.  
  
    
* Once the above step is done, click on the root of the project, find the Gradle tab and click on update gradle, this will start downloading all dependencies.  
  
  
 
* Before running the program, you must specify the profile under which the application will run.  
in the main class located in the  package xx. main Right-click the main class, a window will pop up, find **Run As > Run Configurations**, There another window is shown, here locate the tab that says **(X)=Arguments** (click) and in the text field that says **VM Arguments** specify the profile you need, for example **Dspring.profiles.active = dev** or **Dspring. profiles.active = prod** that refer to the yml files that have been specified as **aplication.dev.yml** and **aplication.prod.yml** in the project, finish by clicking on run or close.  
  
  
    
* If the dependencies have been successfully downloaded and you have configured the profile in the previous step without running the application, right-click the main class located in the main package and run the class (Run as java application). If the project runs successfully, you can navigate to the exposed endpoint [http://localhost:9000/swagger-ui.html](http://localhost:9000/swagger-ui.html)   in your browser

