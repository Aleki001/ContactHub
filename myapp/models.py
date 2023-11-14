from myapp import db
from flask_login import UserMixin


class User(db.Model, UserMixin):
    id = db.Column(db.Integer, primary_key=True)
    username = db.Column(db.String(80), unique=True, nullable=False)
    email = db.Column(db.String(120), unique=True, nullable=False)
    image_file = db.Column(db.String(20), nullable=False, default='default.jpeg')
    password = db.Column(db.String(60), nullable=False)
    contacts = db.relationship('Contact')

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

