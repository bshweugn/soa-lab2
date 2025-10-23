# Как развернуть локально
___

1. Для начала необходимо установить себе wildfly локально и распаковать в удобную директорию. Также для удобства создадим для него 2 переменные окружения. 

```
wget https://github.com/wildfly/wildfly/releases/download/37.0.1.Final/wildfly-37.0.1.Final.zip
unzip wildfly-37.0.1.Final.zip
export WF_HOME=/../wildfly-37.0.1.Final
export STANDALONEXML=$WF_HOME/standalone/configuration/standalone.xml
```

2. В файле standalone.xml можно настроить используемый приложением порт

3. Соберём проект. После чего скопируем полученный .war файл в wildfly в папку deployments  

```
mvn clean package -DskipTests
cp target/{название_вашего_вар_файла}.war $WF_HOME/standalone/deployments/ROOT.war
```
4. После этого можно спокойно запустить wildfly командой
```
$WF_HOME/bin/standalone.sh
```

## Если необходимо задеплоить пересобранный проект в wildfly

На 3 шаге после команды
```
mvn clean package -DskipTests
```
Очмстим deployments командами:
```
rm -f $WF_HOME/standalone/deployments/ROOT.war*
perl -i.bak -0pe 's/<deployments>.*?ROOT\.war.*?<\/deployments>//gs' $STANDALONEXML
```

После этого повторим, копирование варника в папку deployments
```
cp target/{название_вашего_вар_файла}.war $WF_HOME/standalone/deployments/ROOT.war
```

И перезапустим wildfly
```
$WF_HOME/bin/standalone.sh
```
