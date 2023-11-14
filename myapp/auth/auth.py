from flask import render_template, Blueprint, url_for, redirect, request, flash
from myapp.models import User
from myapp import db, bcrypt
from flask_login import current_user, login_user, logout_user



auth = Blueprint('auth', __name__)

@auth.route('/login', methods=['GET', 'POST'])
def login():
    if current_user.is_authenticated:
        return redirect(url_for('views.home'))
    if request.method == 'POST':
        email = request.form.get('email')
        password = request.form.get('password')

        user = User.query.filter_by(email=email).first()
        if user:
            if bcrypt.check_password_hash(user.password, password):
                flash('Logged in successfully', 'success')
                login_user(user, remember=request.form.get('remember'))
                next_page = request.args.get('next')
                return redirect(next_page) if next_page else redirect(url_for('views.home'))
        else:
            flash('PLease check your username and password.', 'danger')


    return render_template('login.html', title="Login", user=current_user)


@auth.route('/register', methods=['GET', 'POST'])
def register():
    if request.method == 'POST':
        email = request.form.get('email')
        username = request.form.get('username')
        password = request.form.get('password')
        confirm_pass = request.form.get('confirm_pass')

        user = User.query.filter_by(email=email).first()
        if user:
            flash('A user with that email already exists.', 'danger')
        elif len(username) < 4:
            flash('Username too short', 'danger')
        elif password != confirm_pass:
            flash('Paswords dont match!', 'danger')
        elif len(password) < 7:
            flash('Password must be at least 7 characters.', 'danger')
        else:
            hashed_password = bcrypt.generate_password_hash(password).decode('utf-8')
            new_user = User(email=email, username=username, password=hashed_password)
            db.session.add(new_user)
            db.session.commit()
            flash('Your Account has been created!! You can now login.', 'success')
            return redirect(url_for('auth.login'))

    return render_template('sign_up.html', title="Sign up", user=current_user)

@auth.route('/logout')
def logout():
    logout_user()
    flash('You have successfully logged out of ContactHub.', 'success')
    return redirect(url_for('auth.login'))


@auth.route('/request')
def forgot_password():
    return render_template('forgot_password.html', user=current_user)

@auth.route('reset')
def password_reset():
    return render_template('password_reset.html', user=current_user)