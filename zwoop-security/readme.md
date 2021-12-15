
Install trident packages: 

trident core: 
```
mvn install:install-file -Dfile=/Users/kimgysen/Documents/projects/zwoop-backend/zwoop-security/src/jars/core/core-0.3.0.jar -DgroupId=org.tron.trident -DartifactId=core -Dversion=0.3.0 -Dpackaging=jar -DlocalRepositoryPath=$HOME/.m2/repository -DpomFile=core/pom.xml
```
trident-abi: 
```
mvn install:install-file -Dfile=/Users/kimgysen/Documents/projects/zwoop-backend/zwoop-security/src/jars/abi/abi-0.3.0.jar -DgroupId=org.tron.trident -DartifactId=abi -Dversion=0.3.0 -Dpackaging=jar -DlocalRepositoryPath=$HOME/.m2/repository -Dpom=abi/pom.xml
```
trident-utils: 
```
mvn install:install-file -Dfile=/Users/kimgysen/Documents/projects/zwoop-backend/zwoop-security/src/jars/utils/utils-0.3.0.jar -DgroupId=org.tron.trident -DartifactId=utils -Dversion=0.3.0 -Dpackaging=jar -DlocalRepositoryPath=$HOME/.m2/repository -Dpom=utils/pom.xml
```
