class User:
    userName = '李黔'
    userAge = 100

    @staticmethod
    def getUserName():
        return User.userName


print(User.getUserName())
