var comments = new Object();

comments.anonymous_comment = function(name) {
	div = document.getElementById('user_name_comment');
	web_div = document.getElementById('website_comment');
	if (div.innerHTML == name) {
		div.innerHTML = "Anonymous";
        web_div.innerHTML = '<input type="text" name="website" size="20" disabled="disabled" class="anonymous_comment_yes">';	
	}
	else {
		div.innerHTML = name;
		web_div.innerHTML = '<input class="anonymous_comment_no" type="text" name="website_comment" size="20" />';
	}
}
