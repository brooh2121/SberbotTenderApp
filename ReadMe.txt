Краткое описание работы приложения SberBotAPP

Каждую минуту стартует бот, переходит по ссылке https://www.sberbank-ast.ru/purchaseList.aspx (Закупки по 44-ФЗ), вбивает в поисковой строке осаго и забирает первые 20 элементов.
Сверяясь, что в базе данных таких элементов нет (берем первые 20 записей из БД) - сверка производится по всему набору элементов (Номер тендера, наименование огранизации, наименование тендера, дата публикации и сумма).
Если совокупность элементов новая - заливаем запись в базу данных.
Работа бота логируется отдельно в файл. (разбивка идет по датам с архивацией старых).

Для настройки приложения на свое окружение достаточно поменять в application.properties внутри jar архива подключения к БД.
И если необходимо логировать в другое место - файл logback.xml

На текущий момент есть одна уязвимость приложения, когда не дожидаясь результата выборки из формы/игнорируя выборку из формы бот забирает первые 20 элементов, которые могут не относится к ОСАГО.
Сделал специальную проверку, что в наименовании тендера явно указано ОСАГО.