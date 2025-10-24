# Как развернуть локально
___

1. Для начала необходимо установить себе payara локально и распаковать в удобную директорию. Также для удобства создадим для неё переменную окружения.  

```
wget https://repo1.maven.org/maven2/fish/payara/distributions/payara/6.2025.9/payara-6.2025.9.zip
unzip payara-6.2025.9.zip
export PAYARA_HOME=../../payara6
```

2. Для запуска сервера воспользуемся следующей командой  

```
${PAYARA_HOME}/bin/asadmin start-domain domain1
```

3. Чтобы запущенные через payara приложения могли корректно подключаться к PostgreSQL передадим ей .jar файл нашего JDBC-драйвера  
```
wget https://repo1.maven.org/maven2/org/postgresql/postgresql/42.7.8/postgresql-42.7.8.jar
mv postgresql-42.7.8.jar $PAYARA_HOME/glassfish/lib
```

После этого payara будет доступна на порту 4848

4. Перейдём по следующему пути Resources -> JDBC -> JDBC Connection Pool. Создадим новый pool с следующими настройками:
![alt text](image1.png)

5. На следующем шаге укажем название подключаемой БД, порт, имя пользователя, пароль и имя сервера.

6. После конфигурации нажимаем кнопу "Finish", созданный пул отобразиться в списке, его можно пингануть для проверки работы и успешной конфигурации. Если будут проблемы payara о них сообщит.

7. Перейдём по следующему пути Resources -> JDBC -> JDBC Resources. Создадим новый. Можно использовать примерно такие настройки:
![alt text](image2.png)

8. Напишем класс конфигуратор, который будет искать указанный нами JNDI
```
@Configuration
public class DataSourceConfigurer {

    private static final String JNDI = "jdbc/orgdirectory_service";

    @Bean(destroyMethod = "")
    public DataSource dataSource() throws DataSourceLookupFailureException {
        JndiDataSourceLookup dataSourceLookup = new JndiDataSourceLookup();
        return dataSourceLookup.getDataSource(JNDI);
    }
}
```

9. Соберём проект. После чего задеплоим полученный .war файл в payara
```
mvn clean package -DskipTests
$PAYARA_HOME/bin/asadmin deploy target/{название_вар_файла}.war
```
10. Если всё успешно, наше приложение отобразится во вкладке Applications

Если проект был пересобран, удобная команда для редеплоя:
```
$PAYARA_HOME/bin/asadmin redeploy target/{название_вар_файла}.war
```

## Деплой на гелиос

1. На гелиос места мало поэтому не получится установить там полноценный payara server. Поэтому лучше всего скачать туда payara-micro 6 версии

```
wget https://repo1.maven.org/maven2/fish/payara/distributions/payara-micro/6.2024.10/payara-micro-6.2024.3.jar

```
2. Собираем war файл нашего проекта и копируем его на гелиос

3. Для успешного запуска payara-micro необходимо созадть jdbc-connection-pool и jdbc ресурс. Для этого создадим файл db-configuration-helios.asadmin. Создадим в нём пул и в проперти укажем, данные для  подключения к БД гелиоса

```
create-jdbc-connection-pool --datasourceclassname org.postgresql.ds.PGSimpleDataSource --restype javax.sql.DataSource --property user={ваш_номер_ису}:password={ваш_пароль_из_pgpass}:DatabaseName=studs:ServerName=pg:PortNumber=5432 orgdirectory_service_pool

create-jdbc-resource --connectionpoolid=orgdirectory_service_pool jdbc/orgdirectory_service

list-jdbc-connection-pools
list-jdbc-resources

```
4. После этого необходимо скопировать данный файл на гелиос, а также необходимо скопировать драйвер используемой БД (postgres).

```
scp -P 2222 db-config-helios.asadmin s368274@helios.cs.ifmo.ru:~/payara
scp -P 2222 postgresql-42.7.3.jar s368274@helios.cs.ifmo.ru:~/payara
```

5. При развертывании инстанса может не хватить выделенного места перед запуском лучше всего расширить metaspace
```
export _JAVA_OPTIONS="-XX:MaxHeapSize=1G -XX:MaxMetaspaceSize=512m"
```

6. Теперь можно запустить payara-micro с помощью команды:
```
java -jar payara-micro-6.2024.3.jar  \
    --nocluster \
    --addlibs payara/postgresql-42.7.3.jar \
    --postbootcommandfile payara/db-config-helios.asadmin \
    --deploy OrgDirectoryService-0.0.1-SNAPSHOT.war \
    --port 8080

```

7. Последним шагом необходимо прокинуть порты
```
ssh -p 2222 s368274@helios.cs.ifmo.ru -L 8080:localhost:8080
```