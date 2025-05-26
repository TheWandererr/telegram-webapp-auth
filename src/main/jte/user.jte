@import by.akonashchenko.telegram.webapp.auth.web.jte.model.User

@param User user

<h1>Данные пользователя</h1>
<p>ID : ${user.getId()}</p>
<p>username : ${user.getUsername()}</p>
<p>Имя : ${user.getFirstName()}</p>
<p>Фамилия : ${user.getLastName()}</p>
<p>Премиум аккаунт : ${user.isPremium()}</p>