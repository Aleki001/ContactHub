from flask import Flask
from flask_sqlalchemy import SQLAlchemy
from flask_bcrypt import Bcrypt
from os import path 
from flask_login import LoginManager
from flask_mail import Mail


db = SQLAlchemy()
DB_NAME = "contacthub.db"
bcrypt = Bcrypt()
mail = Mail()

def create_app():
    app = Flask(__name__)
    app.config['SECRET_KEY'] = 'jsdjgs fjhdhsjsig fghb,cb'
    app.config['SQLALCHEMY_DATABASE_URI'] = f'sqlite:///{DB_NAME}'
    db.init_app(app)
    bcrypt.init_app(app)


    from myapp.auth.auth import auth
    from .views import views
    from myapp.errors.handlers import errors
    from myapp.contacts.routes import contacts

    from .models import User

    app.register_blueprint(views, url_prefix='/')
    app.register_blueprint(auth, url_prefix='/auth/')
    app.register_blueprint(errors)
    app.register_blueprint(contacts)


    create_database(app)

    login_manager = LoginManager()
    login_manager.login_view = 'auth.login'
    login_manager.init_app(app)

    app.config['MAIL_SERVER'] = 'smtp.gmail.com'
    app.config['MAIL_PORT'] = '587'
    app.config['MAIL_USE_TLS'] = True
    app.config['MAIL_USERNAME'] = 'alexapptest123@gmail.com'
    app.config['MAIL_PASSWORD'] = 'hymj lzax oyxv zwgi'
    mail.init_app(app)
    
    

    @login_manager.user_loader
    def load_user(id):
        return User.query.get(int(id))


    return app


def create_database(app):
    if not path.exists('myapp/' + DB_NAME):
        with app.app_context():
            db.create_all()