//  Andy Langton's show/hide/mini-accordion @ http://andylangton.co.uk/jquery-show-hide
// this tells jquery to run the function below once the DOM is ready
define(function(){
	// choose text for the show/hide link - can contain HTML (e.g. an
	// image)
	var showText = 'Show';
	var hideText = 'Hide';
	var is_visible = false;		// initialise the visibility check
	function toggleLink(_this){
		// switch visibility
		is_visible = !is_visible;
		// change the link text depending on whether the element is
		// shown or hidden
		if ($(_this).text() == showText) {
			$(_this).text(hideText);
			$(_this).parent().next('.toggle').slideDown('slow');
		} else {
			$(_this).text(showText);
			$(_this).parent().next('.toggle').slideUp('slow');
		}

		// return false so any link destination is not followed
		return false;
	}
	$(document).ready(function() {
			// append show/hide links to the element directly preceding the
			// element with a class of "toggle"
			$('.toggle').prev().append(' <a href="#" class="toggleLink">' + hideText + '</a>');
			// hide all of the elements with a class of 'toggle'
			$('.toggle').show();
			// capture clicks on the toggle links
			$('a.toggleLink').click(function() {
				return toggleLink(this);
			});
		});
});