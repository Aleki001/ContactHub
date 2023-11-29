class Config:
    DB_NAME = "contacthub.db"
    SQLALCHEMY_DATABASE_URI = f'sqlite:///{DB_NAME}'
    SECRET_KEY = 'jsdjgs fjhdhsjsig fghb,cb'
    MAIL_SERVER = 'smtp.gmail.com'
    MAIL_PORT = '587'
    MAIL_USE_TLS = True
    MAIL_USE_SSL = False
    MAIL_USERNAME = 'alexapptest123@gmail.com'
    MAIL_PASSWORD = 'hymjlzaxoyxvzwgi'