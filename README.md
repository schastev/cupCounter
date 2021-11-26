### Фичи
#### Главный экран
- [x] строка поиска клиента по последним цифрам телефона
- [x] кнопка "Найти"
- [x] список результатов в виде "Имя - телефон". Клик на любой из результатов производит переход на карточку клиента. Если поиск нашел только одного клиента, то сразу открывается его карточка.
- [x] кнопка перехода на экран добавления нового клиента
- [ ] кнопка перехода на экран настроек приложения
#### Экран добавления нового клиента
- [x] поле ввода телефона (с проверкой введенных данных)
- [x] поле ввода имени (с проверкой введенных данных)
- [x] кнопка "Добавить"
#### Карточка с информацией о клиенте
- [x] имя клиента (не редактируется?) + подпись
- [x] телефон клиента (редактируется) + подпись
- [x] кнопка сброса изменений телефона
- [x] дата регистрации клиента (не редактируется) + подпись
- [x] дата последнего визита(не редактируется) + подпись
- [x] уведомление, если клиент не посещал кофейню Х дней (Х задается в настройках)
- [x] поле с текущим количеством купленных (и не списанных) кофейных напитков (не редактируется)
- [x] кнопка "списать" (видна и активна только когда у клиента не менее N кружек)
- [x] уведомление о количестве доступных клиенту бесплатных кружек
- [x] кнопка сброса изменений в количестве кружек
- [x] кнопка "начислить"
- [x] кнопка "сохранить изменения". При нажатии на кнопку появляются всплывающие сообщения об успешных изменениях данных клиента/данных о кружках в зависимости от того, что было изменено. Возможно менять и то, и другое разом.

#### Экран входа в настройки
- [ ] поле для ввода пароля
- [ ] кнопка "Войти"

#### Экран настроек приложения (доступ по паролю администратора)
- [ ] задать Х (см. раздел "Карточка с информацией о клиенте")
- [ ] задать N (см. раздел "Карточка с информацией о клиенте")
- [ ] экспортировать базу данных в .csv
- [ ] импортировать базу данных из .csv
- [ ] удалить клиента
- [ ] настройка регулярных бэкапов на Google Drive

После реализации описанного выше функционала есть план обеспечить взаимодействие одной базы с двумя разными клиентами (когда откроется вторая точка).
