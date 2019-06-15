plugins { 
  java
  eclipse
  id("org.springframework.boot") version "2.1.5.RELEASE" apply false
}

allprojects {
   group = "it.discovery"
}

subprojects {  
   apply(plugin = "java")
   //apply(plugin = "org.springframework.boot")

   java.sourceCompatibility = JavaVersion.VERSION_11
   java.targetCompatibility = JavaVersion.VERSION_11

   repositories {
     jcenter()
   }

   var springBootVersion = "2.1.5.RELEASE"
   
   dependencies {
        implementation(platform("org.springframework.boot:spring-boot-dependencies:$springBootVersion"))

        compile("org.springframework.boot:spring-boot-devtools")        
        compile("org.springframework.boot:spring-boot-starter-web")
        compile("org.apache.commons:commons-lang3")
	compile("org.hibernate:hibernate-entitymanager")

        runtime("javax.xml.bind:jaxb-api:2.3.0")
        runtime("javax.annotation:javax.annotation-api:1.3.1")

        compileOnly("org.projectlombok:lombok:1.18.8")
        annotationProcessor("org.projectlombok:lombok:1.18.8")

        testCompile("org.junit.jupiter:junit-jupiter-api")
        testRuntime("org.junit.jupiter:junit-jupiter-engine")
        testCompile("org.springframework.boot:spring-boot-starter-test")

   } 
}

tasks.test {
    useJUnitPlatform()
}