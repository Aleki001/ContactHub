document.addEventListener("DOMContentLoaded", function(event) {
   
        const showNavbar = (toggleId, navId, bodyId, headerId) =>{
        const toggle = document.getElementById(toggleId),
        nav = document.getElementById(navId),
        bodypd = document.getElementById(bodyId),
        headerpd = document.getElementById(headerId)
        
        // Validate that all variables exist
        if(toggle && nav && bodypd && headerpd){
        toggle.addEventListener('click', ()=>{
        // show navbar
        nav.classList.toggle('show')
        // change icon
        toggle.classList.toggle('bx-x')
        // add padding to body
        bodypd.classList.toggle('body-pd')
        // add padding to header
        headerpd.classList.toggle('body-pd')
        })
        }
        }
        
        showNavbar('header-toggle','nav-bar','body-pd','header')
        
    /*===== LINK ACTIVE =====*/
    const linkColor = document.querySelectorAll('.nav_link');

    function setActiveLink() {
      const currentUrl = window.location.href;
      linkColor.forEach(link => {
        if (link.href === currentUrl) {
          link.classList.add('active');
        } else {
          link.classList.remove('active');
        }
      });
    }
  
    setActiveLink(); // Set the active link on initial page load
  
    // Add event listener to handle active link on every page change
    window.addEventListener('popstate', setActiveLink);

}
)