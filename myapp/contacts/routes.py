import secrets
import os
from flask import current_app, flash, redirect, url_for, request, render_template, Blueprint
from werkzeug.utils import secure_filename
from PIL import Image
from flask_login import current_user, login_required
from myapp.forms import ContactForm
from myapp.models import User, Contact
from myapp import db

contacts = Blueprint('contacts', __name__)


def save_contact_picture(form_picture):
    random_hex = secrets.token_hex(8)
    _, f_ext = os.path.splitext(form_picture.filename)
    picture_fn = random_hex + f_ext
    picture_path = os.path.join(current_app.root_path, 'static/contact_images', picture_fn)

    output_size = (125, 125)
    i = Image.open(form_picture)
    i.thumbnail(output_size)
    i.save(picture_path)

    return picture_fn


@contacts.route('/new_contact', methods=['GET', 'POST'])
@login_required
def new_contact():
    form = ContactForm()

    if form.validate_on_submit():
        # Handle form submission for creating a new contact
        new_contact = Contact(
            full_name=form.full_name.data,
            email=form.email.data,
            address=form.address.data,
            phone_no=form.phone_no.data,
            user_id=current_user.id 
        )

        # Handle file upload for the new contact
        if form.picture.data:
            picture_file = save_contact_picture(form.picture.data)
            new_contact.image_file = picture_file

        db.session.add(new_contact)
        db.session.commit()

        flash('New contact created successfully!', 'success')
        return redirect(url_for('views.home'))

    return render_template('new_contact.html', form=form, user=current_user)




@contacts.route('/contact/<int:contact_id>', methods=['GET', 'POST'])
def contact(contact_id):
    contact = Contact.query.get_or_404(contact_id)
    form = ContactForm()

    if form.validate_on_submit():
        # Handle form submission
        contact.full_name = form.full_name.data
        contact.email = form.email.data
        contact.address = form.address.data
        contact.phone_no = form.phone_no.data

        # Handle file upload
        if form.picture.data:
            file = form.picture.data
            filename = secure_filename(file.filename)
            file.save(os.path.join(current_app.root_path, 'static/contact_images', filename))
            contact.image_file = filename

        db.session.commit()
    elif request.method == 'GET':
        form.full_name.data = contact.full_name
        form.email.data = contact.email
        form.address.data = contact.address
        form.phone_no.data = contact.phone_no
        form.picture.data = contact.image_file


    image_file = url_for('static', filename='contact_images/' + contact.image_file)

    return render_template('contact.html', image_file=image_file, form=form, contact=contact, user=current_user, title="Contact Page")

@contacts.route("/post/<int:contact_id>/delete", methods=['POST'])
@login_required
def delete_contact(contact_id):
    contact = Contact.query.get_or_404(contact_id)
    db.session.delete(contact)
    db.session.commit()
    flash(' A contact has been deleted', 'success')
    return redirect(url_for('views.home'))