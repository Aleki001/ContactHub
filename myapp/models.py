from myapp import db
from flask_login import UserMixin
from itsdangerous import TimedJSONWebSignatureSerializer as Serializer
from myapp import create_app

class User(db.Model, UserMixin):
    id = db.Column(db.Integer, primary_key=True)
    username = db.Column(db.String(80), unique=True, nullable=False)
    email = db.Column(db.String(120), unique=True, nullable=False)
    image_file = db.Column(db.String(20), nullable=False, default='default.jpeg')
    password = db.Column(db.String(60), nullable=False)
    contacts = db.relationship('Contact')

    def get_reset_token(self, expires_sec=1800):
        s = Serializer(create_app().config['SECRET_KEY'], expires_sec)
        return s.dumps({'user_id': self.id}).decode('utf-8')
    

    @staticmethod
    def verify_reset_token(token):
        s = Serializer(create_app().config('SECRET_KEY'))
        try:
            user_id = s.loads(token)['user_id']
        except:
            return None
        return User.query.get(user_id)


    def __repr__(self):
        return f"User('{self.username}', '{self.email}', '{self.image_file}')"
    


class Contact(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    full_name = db.Column(db.String(80), unique=False, nullable=False)
    email = db.Column(db.String(120), unique=False, nullable=False)
    address = db.Column(db.String(120), unique=False, nullable=False)
    phone_no = db.Column(db.Integer, unique=False, nullable=False)
    image_file = db.Column(db.String(20), nullable=False, default='default.jpeg')
    user_id = db.Column(db.Integer, db.ForeignKey('user.id'), nullable=False)

    def __repr__(self):
        return f"Contact('{self.full_name}', '{self.email}', '{self.image_file}', '{self.address}', '{self.phone_no}')"


