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

## Деплой на гелиос  

1. Установим wildfly последней версии на гелиос и распакуем его
```
wget https://github.com/wildfly/wildfly/releases/download/37.0.1.Final/wildfly-37.0.1.Final.zip
unzip wildfly-37.0.1.Final.zip
```
2. Соберём варник и скопируем его на гелиос
```
mvn clean package -DskipTests
scp -P 2222 target/ROOT.war s368274@helios.cs.ifmo.ru:~/
```

3. Скопируем варник в папку deployments в wildfly
```
cp ~/ROOT.war ~/wildfly-37.0.1.Final/standalone/deployments/
```

4. При развертывании инстанса может не хватить выделенного места перед запуском лучше всего расширить metaspace
```
export _JAVA_OPTIONS="-XX:MaxHeapSize=1G -XX:MaxMetaspaceSize=512m"
```

5. Запустим wildfly, но для этого поменяем стандартные порты на те которые будут более удобны и не будут вызывать конфликтов

```
cd ~/wildfly-37.0.1.Final/bin
./standalone.sh -b 0.0.0.0 -Djboss.http.port=28228 -Djboss.https.port=28443 -Djboss.management.http.port=29990

```

6. Последним шагом прокиним порты на локалку
```
ssh -p 2222 s368274@helios.cs.ifmo.ru -L 8081:localhost:28228
```