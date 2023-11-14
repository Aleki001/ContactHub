from flask import render_template, Blueprint, flash, url_for, request, redirect, current_app
from flask_login import current_user, login_required
from . import db
from .forms import UpdateProfileForm, ContactForm
import os
import secrets
from .models import User, Contact
from PIL import Image


views = Blueprint('views', __name__)

@views.route('/')
@login_required
def home ():
    page = request.args.get('page', 1, type=int)
    contacts = Contact.query.filter_by(user_id=current_user.id).paginate(page=page, per_page=9)

    return render_template('index.html', contacts = contacts, title='Home', user=current_user)

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

