from flask import render_template, Blueprint, url_for, redirect, request, flash
from myapp.models import User
from myapp import db, bcrypt, mail
from flask_login import current_user, login_user, logout_user
from myapp.forms import RequestResetForm, ResetPasswordForm
from flask_mail import Message


auth = Blueprint('auth', __name__)

@auth.route('/login', methods=['GET', 'POST'])
def login():
    if current_user.is_authenticated:
        return redirect(url_for('views.index'))
    if request.method == 'POST':
        email = request.form.get('email')
        password = request.form.get('password')

        user = User.query.filter_by(email=email).first()
        if user:
            if bcrypt.check_password_hash(user.password, password):
                flash('Logged in successfully', 'success')
                login_user(user, remember=request.form.get('remember'))
                next_page = request.args.get('next')
                return redirect(next_page) if next_page else redirect(url_for('views.index'))
        else:
            flash('Please check your username and password.', 'danger')


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

def send_reset_email(user):
    token = User.get_reset_token()
    msg = Message('Password Reset Request',
                   sender='alexapptest123@gmail.com',
                    recipients=[user.email])
    msg.body = f'''To reset your password visit the following link
    {url_for('reset_token', token=token, _external=True)}
    if you did not make this request ignore this email and no changes will be made 
    '''
    mail.send(msg)


@auth.route('/reset_password', methods=['GET', 'POST'])
def reset_request():
    if current_user.is_authenticated:
        return redirect(url_for('views.index'))
    form = RequestResetForm()
    if form.validate_on_submit():
        user = User.query.filter_by(email=form.email.data).first()  
        send_reset_email(user)
        flash('An email has been sent with instructions to reset your password', 'info')
        return redirect(url_for('login'))
    return render_template('forgot_password.html', user=current_user, title='Reset Password', form=form)


@auth.route('/reset_password/<token>', methods=['GET', 'POST'])
def reset_token(token):
    if current_user.is_authenticated:
        return redirect(url_for('views.index'))
    user = User.verify_reset_token(token)
    if user is None:
        flash('That is an invalid or expired token', 'warning')
        return redirect(url_for('reset_request'))
    form = ResetPasswordForm()
    if form.validate_on_submit():
        hashed_password = bcrypt.generate_password_hash(form.password.data).decode('utf-8')
        user.password = hashed_password
        db.session.commit()
        flash('Your password has been updated! You are now able to log in', 'success')
        return redirect(url_for('login'))
    return render_template('password_reset.html', user=current_user, title='Reset Password', form=form)