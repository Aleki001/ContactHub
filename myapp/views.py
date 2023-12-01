from flask import render_template, Blueprint, flash, url_for, request, redirect, current_app
from flask_login import current_user, login_required
from . import db, bcrypt
from .forms import UpdateProfileForm, ContactForm
import os
import secrets
from .models import User, Contact
from PIL import Image


views = Blueprint('views', __name__)

@views.route('/')
def landing():
    return render_template('landingpage.html', title='Home')

@views.route('/contacts')
@login_required
def index():
    page = request.args.get('page', 1, type=int)
    contacts = Contact.query.filter_by(user_id=current_user.id).paginate(page=page, per_page=9)

    return render_template('index.html', contacts = contacts, title='Contacts', user=current_user)

@views.route("/account", methods=['GET', 'POST'])
@login_required
def profile():
    form = UpdateProfileForm()
    if form.validate_on_submit():
        if form.picture.data:
            picture_file = save_picture(form.picture.data)
            current_user.image_file = picture_file
        current_user.username = form.username.data
        current_user.email = form.email.data
        db.session.commit()
        flash('Your account has been updated!', 'success')
        return redirect(url_for('views.profile'))
    elif request.method == 'GET':
        form.email.data = current_user.email
        form.username.data = current_user.username
        form.email.data = current_user.email
    image_file = url_for('static', filename='profile_images/' + current_user.image_file)
    return render_template('profile.html', title='Profile', image_file=image_file, form=form, user=current_user)



def save_picture(form_picture):
    random_hex = secrets.token_hex(8)
    _, f_ext = os.path.splitext(form_picture.filename)
    picture_fn = random_hex + f_ext
    picture_path = os.path.join(current_app.root_path, 'static/profile_images', picture_fn)

    output_size = (125, 125)
    i = Image.open(form_picture)
    i.thumbnail(output_size)
    i.save(picture_path)

    return picture_fn

@views.route('/contacts', methods=['POST', 'GET'])
@login_required
def search_contact():
    if request.method == 'POST':
        query = request.form['query']
        page = request.args.get('page', 1, type=int)
        
        contacts = Contact.query.filter_by(user_id=current_user.id).filter(Contact.full_name.like(f"%{query}%")).paginate(page=page, per_page=9)
        
        return render_template('search_contact.html', contacts=contacts, title='Home', user=current_user, query=query)

    return render_template('index.html', title='Home', user=current_user)


@views.route('/change-password', methods=('POST', 'GET'))
@login_required
def change_pass():
    if request.method == 'POST':
        old_pass= request.form.get('oldpassword')
        new_pass = request.form.get('newpassword')
        confirm_pass = request.form.get('confirmpassword')

        if not bcrypt.check_password_hash(current_user.password, old_pass):
            flash('Your old password is incorrect. Try again', 'danger')
        elif new_pass != confirm_pass:
            flash('New password and confirm password do not match! Try again.', 'danger')
        else:
            hashed_pass = bcrypt.generate_password_hash(new_pass).decode('utf-8')
            current_user.password = hashed_pass
            db.session.commit()
            flash('Password changed successfully!', 'success')
            return redirect(url_for('views.index'))
    return render_template('change_password.html', user=current_user, title = 'Change Password')


