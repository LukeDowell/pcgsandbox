$( document ).ready(function() {
    var protocol    = window.location.protocol;
    if ( typeof(protocol) == 'undefined' || protocol == '' ) {
        protocol = 'http:';
    }
	var referer 	= encodeURIComponent(document.referrer);
	var redirectTo 	= encodeURIComponent(protocol + '//' + window.location.host + '/checkauthIFrame.htm');
	var winLocation = window.location.href;
	$.ajax({
		url: '/checkauth.php',
		data: {'referer': referer, 'redirectTo': redirectTo},
		success: function(data) {
		   if ( typeof(data) != 'undefined' && data != null && typeof(data.redirectTo) != 'undefined' && data.redirectTo != null && winLocation.match(/sso\/login.php/gi) == null ) {
				$('body').append('<iframe style="display:none" id="checkAuthFrame" height="100" frameborder="0" src="'+data.redirectTo+'"></iframe>'); 
				$('#checkAuthFrame').load(function() {
					var cLocation = $(this).contents().find('#cLocation').html();
					//Get URL from param
					var token = $.getUrlVar(cLocation, 'token');
					if ( token !== undefined && token != '' ) {
						//If token is exist then do second checkauth call to process the token
						var welcomeUrl = encodeURIComponent(protocol + '//' + window.location.host + '/welcome.php');
						$.ajax({
							type: "GET",
							url: "/checkauth.php",
							data: {'referer': referer, 'redirectTo': welcomeUrl, 'token': token},
							success: function(data){
								$.welcomeCall ( welcomeUrl );
								
								//Authenticate into Gama account for who logged in via SSO
								$.gamaAccCall ();
							},
							dataType: 'json',
							cache: false
						});
					}
					else {
						$.welcomeCall ();
					}
				});
		   }
		   else {
				$.welcomeCall ();
			}
		},
		dataType: 'json',
		cache: false
	});
	
	var articleUri = null;
	$.welcomeCall = function ( welcomeUrl ) {
		
		if ( typeof ( welcomeUrl ) == 'undefined' ) {
			welcomeUrl = protocol + '//' + window.location.host + '/welcome.php';
		}
		$.ajax({
			url: decodeURIComponent(welcomeUrl),
			success: function(data){
				//Update welcome bar
				$('.welcome').html(data);

				//Update mobile welcome bar
				if ( winLocation.match(/sso\/login.php/gi) == null ) {
					if ( typeof(authenticateUserCheck) != 'undefined' && authenticateUserCheck != '' ) {
						$('#login_phone').html('<a href="http://' + window.location.host + '/logout?redirect=' + window.location.href + '">LOGOUT</a>');
					}
					else {
						$('#login_phone').html('<a href="http://' + window.location.host + '/sso/login.php?from=' + window.location.href + '">LOGIN</a>');
					}
				}

				//Initialize lightbox
				$('.welcome, form#LoginForm').nextGenInit({
					width: 780,
					siteUrlPrefix: '',
					siteRedirectPrefix: ''
				});
				
				//Load comments
				if ( $('#dynamiccomments') && $('#dynamiccomments').length ) {
					
					var articleId  = $('#dynamiccomments span#cmtArticleId').text();
					var story_type = $('#dynamiccomments span#cmtStory_type').text();
					articleUri = $('#dynamiccomments span#cmtArticleUri').text();
					
					$.loadComments (articleId, story_type, articleUri);
					//Load edit links
					if ( $('#edit_post_link') && $('#edit_post_link').length ) {
						$.loadEditLinks (articleId, story_type);
					}
				}
 
				//Check for auto open light box
				if ( isAutoOpenLightBox () ) {
					if ( $('a.myacclnk').length != 0 ) {
						$('a.myacclnk').trigger('click');
					}
					else if ( $('a#lbreglnk').length != 0 ) {
						$('a#lbreglnk').trigger('click');
					}
				}
				
				//Load Omniture
				$.loadOmniture ();
			},
			cache: false
		});
	};
	
	$.gamaAccCall = function ( ) {
		$.ajax({
			url: '/registration/managegamaaccount',
			success: function ( data ) {
				//If there is any error in loggin into gama account then logout from NG acc as well
				if ( data.error && data.logoutUrl ) {
					window.location = data.logoutUrl;
				}
			},
			cache: false
		});
	};
	
	$.getUrlVar = function(url, key){
		var result = new RegExp(key + "=([^&]*)", "i").exec(url);
		return result && unescape(result[1]) || "";
	};
	
	$.loadComments = function (articleId, story_type, articleUri) {
		$.ajax({
			url:'/loadcommentdetails?story=' + articleId + '&articleUri=' + articleUri + '&story_type=' + story_type,
			cache: false,
			contentType: "application/html; charset=utf-8",
			dataType: 'json',
			success:function(result){
				var commentsData = buildComments ( result );
				var commentBox	 = buildNewCommentBox ( result );
				$("#dynamiccomments").html ( '<a style="font-weight: bold; font-size: 16px;" name="comments">Comments</a><br />' + commentsData + commentBox );
				var target = $.url().attr('fragment');
				if ( $('#' + target).length == 0 && /comment/i.test(target) ) {
				  target = 'dynamiccomments';
				}
				if ( $('#' + target).length > 0 ) {
				  $('html, body').stop().animate({
				   scrollTop: $('#' + target).offset().top
				  }, 200);
				}
			}
		});
	}
	
	$.loadEditLinks = function (articleId, story_type) {
	
		var url = '/adminarticlelinks?story=' + articleId + '&story_type=' + story_type;
		if ( story_type == 'blog' ) {
			var author_id = '';
			if ( $('#author_id') && $('#author_id').length ) {
				author_id = $('#dynamiccomments span#author_id').text();
				url = url + '&author_id=' + author_id;
			}
			var author_user_id = '';
			if ( $('#author_user_id') && $('#author_user_id').length ) {
				author_user_id = $('#dynamiccomments span#author_user_id').text();
				url = url + '&author_user_id=' + author_user_id;
			}
		}
		$.ajax({
			url: url,
			success:function(result){
				$('#edit_post_link').html(result);
			}
		});
	}
	
	$.loadOmniture = function () {
		var referer   = '';
        var eVar23 	  = '';
        var userAgent = '';

		$.ajax({
		   type: 'GET',
		   url: '/omniture',
		   data: 'referer=' + encodeURIComponent(document.referrer),
		   success: function(data) {
			   if (data != null) {
					referer = data.referer;
					eVar23 = data.eVar23;
					userAgent = data.userAgent;
				}
		   },
		   dataType: "json",
		   async: false,
		   cache: false
		});
	}

	/**
	* Check for auto open lightbox
	*/
    function isAutoOpenLightBox () {
	   var str 		= window.location.href;
	   var profile 	= str.match(/sso\/profile.php/gi);
	   var join 	= str.match(/sso\/join.php/gi);
	   
	   if ( ( profile != null && $('#delete_yes').length == 0 ) || ( join != null && $('#employerHidForm').length != 0 ) ) { 
		   return true;
	   }
	   return false;
    }
	
	$('body').on('click', '.viewTopCmts a', function (){
		var cmtIds 		= $(this).parents('.viewTopCmts').find('span').text();
		var viewTopCmts = $(this).parents('.viewTopCmts');
		if ( cmtIds.length ) {
			viewTopCmts.css('text-align','center').html ('<img style="padding-top: 2px; padding-right: 5px; padding-bottom: 5px;" alt="loader image" src="/ajax-loader.gif"/>');
			$.ajax({
				url:'/loadcommentdetails?cmtIds=' + cmtIds + '&articleUri=' + articleUri,
				cache: false,
				contentType: "application/html; charset=utf-8",
				dataType: 'json',
				success:function(result){
					var commentsData = buildComments ( result );
					viewTopCmts.replaceWith ( commentsData );
					if ( $('#dynamiccomments .viewTopCmts').last().length == 0 ) {
						$('#dynamiccomments .viewTopCmtsHelpTxt').hide();
					}
					else {
						$('#dynamiccomments .viewTopCmts').last().show();
					}
				}
			});
		}
	});

	function buildComments ( result ) {
		var commentsData = '';
		
		//View top comments links
		if ( result.topCmtIds != null && result.topCmtIds.length ) {
			commentsData += '<div class="viewTopCmtsHelpTxt">You are only viewing a portion of the comments on this story.</div>';
			$.each( result.topCmtIds, function ( key, value ) {
				if ( key == result.topCmtIds.length - 1 ) {
					commentsData += '<div class="viewTopCmts">';
				}
				else {
					commentsData += '<div class="viewTopCmts" style="display:none;">';
				}
				commentsData += '<a href="javascript:void(0)">[View Older Comments]</a>';
				commentsData += '<span style="display:none;">' + value + '</sapn>';
				commentsData += '</div>';
			});
		}
		
		if ( result.comments.comment_data != null && result.comments.comment_data.length ) {
			$.each( result.comments.comment_data, function ( key, value ) {
				var comment_type = 'parentComment';
				var align		 = '';
				if ( value.comment_type == 'reply' ) {
					comment_type  = 'replyComment';
					align		  = ' align="right" ';
				}
				
				commentsData += '<div class="'+ comment_type +'" '+ align +'>';
				commentsData += ' <table id="comment'+ value.id + '" cellpadding="0" cellspacing="0" class="commentBox" border="0">';
				commentsData += '   <tr>';
				commentsData += '      <td colspan="2">';
				commentsData += '         <hr noshade size="1" class="hr_comment">';
				commentsData += '      </td>';
				commentsData += '   </tr>';
				commentsData += '   <tr>';
				commentsData += '      <td style="background-color:#dcebdc; padding: 5px; text-align: left;">';
											if ( value.anonymous == 1 ) {
												commentsData += 'Anonymous<br />';
											}
											else {
												if ( value.blog_id ){
													commentsData += '<a href="'+ value.author_url +'">'+ value.firstname + ' ' + value.lastname +'</a><br />';
												}
												else {
													commentsData += value.firstname + ' ' + value.lastname +'<br />';
												}
											}
				commentsData += '      </td>';
				commentsData += '      <td style="background-color:#dcebdc; padding: 5px;">';
				commentsData += '         <div class="date_comment">';
											commentsData += '<a href="#comment'+ value.id +'">'+ value.date_time +' PST </a>';
											if ( typeof (result.comments.admin) != 'undefined' && result.comments.admin != '' ) {
												commentsData += ' | <a href="?story='+ result.comments.story_id + '&comment_delete='+ value.id +'" onclick="return confirm(\'Deleting comment will also delete subsequent replies. Are you sure you want to delete?\')">  delete</a>';
											}
											if ( ( typeof (result.comments.admin) != 'undefined' && result.comments.admin != '' ) || value.user_id == result.comments.user_id ) {
												commentsData += ' | <a href="javascript:void(0)" onclick="editComment('+ value.id + ')"> edit</a>';
											}
											if ( typeof (result.comments.admin) != 'undefined' && result.comments.admin != '' ) {
												commentsData += ' | <a href="?story='+ result.comments.story_id +'&comment_ban='+ value.id +'" onclick="return confirm(\'Ban will remove all user comments and ban user permanently. Are you sure you want to mark user as Banned?\')">  ban</a>';
											}
				commentsData += '         </div>';
				commentsData += '      </td>';
				commentsData += '   </tr>';
				commentsData += '</table>';
				
				commentsData += '<table cellpadding="0" cellspacing="0" border="0" class="commentBox" style="margin-bottom: 0px; margin-top:2px;">';
				commentsData += '   <tr>';
				commentsData += '      <td style="width: 80px; text-align: center;" valign="top">                     ';
											if ( value.portrait_path ) {
												if ( value.legacy_blog_id ) {
													id = value.legacy_blog_id;
												}
												else {
													id = value.blog_id;
												}
												src = '/blogs/edit/img/portrait/' + id + '/thumb_' + value.portrait_path;
											}
											else {
												src =  'http://twimgs.com/gamasutra/images/questionmark.jpg';
											}
				commentsData += '         <img style="padding-top: 2px; padding-right: 5px; padding-bottom: 5px;" alt="profile image" src="'+ src +'"/>';
				commentsData += '      </td>';
				commentsData += '      <td valign="top" style="text-align:left;">';
				commentsData += '         <div id="comment'+ value.id +'_show" class="single_comment">';
											commentsData += value.comment_body;
				commentsData += '         </div>';
										  if ( ( typeof (result.comments.admin) != 'undefined' && result.comments.admin != '' ) || value.user_id == result.comments.user_id ) {
				commentsData += '         <div id="comment'+ value.id +'_edit" class="single_comment" style="display:none;">';
				commentsData += '            <form name="comments'+ value.id +'_edit_submit" action="'+ result.articleUri +'" method="post">';
				commentsData += '               <textarea class="comment_textarea" name="comment_body" rows="8" style="width: 99%">'+ value.original_comment_body +'</textarea>';
				commentsData += '               <input type="hidden" name="comment_edit" value="'+ value.id +'" />';
				commentsData += '               <a href="javascript:void(0)" onclick="document.comments'+ value.id +'_edit_submit.submit(); return false;">Done</a> | ';
				commentsData += '               <a href="javascript:void(0)" onclick="cancelEditComment('+ value.id +')">Cancel</a>';
				commentsData += '            </form>';
				commentsData += '         </div>';
										  }
				commentsData += '      </td>';
				commentsData += '   </tr>';
				commentsData += '   <tr>';
				commentsData += '      <td style="width: 80px; text-align: center;" valign="top">';
				commentsData += '      </td>';
				commentsData += '      <td valign="top" style="text-align:left;">';
				commentsData += '         <div id="comment'+ value.id +'_replyLink">';
											if( result.comments.user_id ) {
				commentsData += '            <form name="comments'+ value.id +'_unlike_submit" action="'+ result.articleUri +'#comment'+ value.id +'" method="post" style="display:inline;">';
				commentsData += '               <input type="hidden" name="comment_unlike" value="'+ value.id +'" />';
				commentsData += '            </form>';
				commentsData += '            <form name="comments'+ value.id +'_like_submit" action="'+ result.articleUri +'#comment'+ value.id +'" method="post" style="display:inline;">';
				commentsData += '               <input type="hidden" name="comment_like" value="'+ value.id +'" />';
				commentsData += '            </form>';
				commentsData += '            <a href="javascript:void(0)" onclick="replyComment('+ value.id +')">Reply</a> ';
												if ( typeof (value.comment_likes) != 'undefined' && value.comment_likes.indexOf ( result.comments.user_id ) != -1 ) {
													
				commentsData += '| <a href="javascript:void(0)" onclick="document.comments'+ value.id + '_unlike_submit.submit(); return false;">Unlike</a>';
				commentsData += '               	<div style="display:inline; float:right;">';
													if ( value.num_likes > 0 ) {
														commentsData += value.num_likes;
														commentsData += '&nbsp;<img src="http://twimgs.com/gamasutra/images/thumbs_up.png" height="13" border="0" alt="likes">';
													}
				commentsData += '                </div>';
												}
												else {
													if (value.memberId != result.comments.user_id){
														commentsData += '| <a href="javascript:void(0)" onclick="document.comments'+ value.id +'_like_submit.submit(); return false;">Like</a>';
													}
													commentsData += '<div style="display:inline; float:right;">';
													if ( value.num_likes > 0 ) {
														commentsData += value.num_likes;
														commentsData += '&nbsp;<img src="http://twimgs.com/gamasutra/images/thumbs_up.png" height="13" border="0" alt="likes">';
													}
													commentsData += '</div>';
												}
											}
											else {
												commentsData += '<div id="no_reply_button">';
												commentsData += '<a href="/sso/login.php?from='+ result.baseUrl + result.articleUri + '">Login to Reply or Like</a>';
												commentsData += '<div style="display:inline; float:right;">';
												if ( value.num_likes > 0 ) {
													commentsData += value.num_likes;
													commentsData += '&nbsp;<img src="http://twimgs.com/gamasutra/images/thumbs_up.png" height="13" border="0" alt="likes">';
												}
												commentsData += '</div>';
												commentsData += '</div>';
											}
				commentsData += '         </div>';
				commentsData += '         <div id="comment'+ value.id +'_replyForm" style="display:none;">';
				commentsData += '            <div id="website_comment">';
												if ( typeof (value.comment_likes) != 'undefined' && value.comment_likes.indexOf ( result.comments.user_id ) != -1 ) {
													commentsData += '<a href="javascript:void(0)" onclick="document.comments'+ value.id +'_unlike_submit.submit(); return false;">Unlike</a>';
													commentsData += '<div style="display:inline; float:right;">';
													if ( value.num_likes > 0 ) {
														commentsData += value.num_likes;
														commentsData += '&nbsp;<img src="http://twimgs.com/gamasutra/images/thumbs_up.png" height="13" border="0" alt="likes">';
													}
													commentsData += '</div>';
												}
												else {
													if ( value.memberId != result.comments.user_id ) {
														commentsData += '<a href="javascript:void(0)" onclick="document.comments'+ value.id +'_like_submit.submit(); return false;">Like</a>';
													}
													commentsData += '<div style="display:inline; float:right;">';
													if ( value.num_likes > 0) {
														commentsData += value.num_likes;
														commentsData += '&nbsp;<img src="http://twimgs.com/gamasutra/images/thumbs_up.png" height="13" border="0" alt="likes">';
													}
													commentsData += '</div>';
												}
				commentsData += '            </div>';
				commentsData += '            <form name="comments'+ value.id +'_reply_submit" action="'+ result.articleUri +'#comment'+ value.id +'" method="post">';
				commentsData += '               <textarea class="comment_textarea" name="comment_body" rows="8" style="width: 99%"></textarea>';
				commentsData += '               <input type="hidden" name="comment_reply" value="'+ value.id +'" />';
				commentsData += '               <input type="hidden" name="comment_type" value="reply" />';
				commentsData += '               <a href="javascript:void(0)" onclick="trackComment(); document.comments'+ value.id +'_reply_submit.submit(); return false;"> Submit Reply </a> |';
				commentsData += '               <a href="javascript:void(0)" onclick="cancelReplyComment('+ value.id +')">Cancel Reply</a>';
				commentsData += '            </form>';
				commentsData += '         </div>';
				commentsData += '      </td>';
				commentsData += '   </tr>';
				commentsData += ' </table>';
				commentsData += '</div>';

			});
		}

		return commentsData;
	}
	
	function buildNewCommentBox ( result ) {
		var commentsData = '';
		if ( authenticateUserCheck != '' ) {
			commentsData += '<div id="comment_form">';
			commentsData += '   <hr noshade size="1" class="hr_comment">';
			commentsData += '   <br />';
			commentsData += '   <form name="comments_submit" action="'+ result.articleUri +'" method="post">';
			commentsData += '      <table cellspacing="0" cellpadding="0" border="0">';
			commentsData += '         <tr>';
			commentsData += '            <td class="td_comment_name">';
			commentsData += '               <div id="user_name_comment">';
			commentsData += 					result.comments.user_name;
												if ( typeof (result.comments.commentAppOn) != 'undefined' && result.comments.commentAppOn == 'true' ) {
													commentsData += '<br /><font color="red">Since you are a first time poster, your comment will be queued for approval. Expect to see it appear in the next 24 hours. When your first comment is approved, you will be able to post comments going forward immediately. Please adhere to Gamasutra <a href="http://www.gamasutra.com/static2/comment_guidelines.html">comment guidelines</a>.</font>';
												}
			commentsData += '               </div>';
			commentsData += '            </td>';
			commentsData += '            <td class="td_comment_anonymous" valign="top">';
			commentsData += '            </td>';
			commentsData += '            <td>';
			commentsData += '               &nbsp;';
			commentsData += '            </td>';
			commentsData += '         </tr>';
			commentsData += '         <tr>';
			commentsData += '            <td class="td_blank_comment_website">';
			commentsData += '               <div id="website_comment">';
			commentsData += '               </div>';
			commentsData += '            </td>';
			commentsData += '            <td>';
			commentsData += '               <div class="td_comment_website">';
			commentsData += '               </div>';
			commentsData += '            </td>';
			commentsData += '            <td>&nbsp;</td>';
			commentsData += '         </tr>';
			commentsData += '      </table>';
			commentsData += '      <textarea class="comment_textarea" name="comment_body" rows="8" style="width: 99%"></textarea>';
			commentsData += '      <input type="hidden" name="comment_type" value="comment" />';
			commentsData += '      <br />';
			commentsData += '      <br />';
			commentsData += '      <div class="comment_submit">';
			commentsData += '         <a class="noline" onclick="document.comments_submit.submit(); javascript:trackComment();return false;"  href="#">Submit Comment</a>';
			commentsData += '      </div>';
			commentsData += '      <br /><br />';
			commentsData += '   </form>';
			commentsData += '</div>';
		}
		else {
			commentsData += '<div id="no_comment_form">';
			commentsData += '   <br />';
			commentsData += '   <span class="comment_login">';
			commentsData += '   <a href="/sso/login.php?from='+ result.baseUrl + result.articleUri + '">';
			commentsData += '   <img src="http://twimgs.com/gamasutra/images/btn_comment.gif" width="113" height="16" border="0" alt="Login to Comment" /></a>';
			commentsData += '   </span>';
			commentsData += '   <br />';
			commentsData += '</div>';
		}
		commentsData += '<br />';
		
		return commentsData;
	}

	//CASL questions feildset toggle
	$('body').on('change', 'select#107', function (){
		if ($(this).val() == 124){
			$('fieldset.custom-question').show();
		}
		else {
			$('fieldset.custom-question').hide();
		}
	});
});

function editComment(c) {
	document.getElementById('comment'+c+'_show').style.display = 'none';
	document.getElementById('comment'+c+'_edit').style.display = 'block';
}

function cancelEditComment(c) {
	document.getElementById('comment'+c+'_show').style.display = 'block';
	document.getElementById('comment'+c+'_edit').style.display = 'none';
}

function replyComment(c) {
  if(authenticateUserCheck) {
	  document.getElementById('comment'+c+'_replyLink').style.display = 'none';
	  document.getElementById('comment'+c+'_replyForm').style.display = 'block';
  } else {
	  window.location.assign('/sso/login.php?from=' + document.URL);
  }   
}

function cancelReplyComment(c) {
	document.getElementById('comment'+c+'_replyLink').style.display = 'block';
	document.getElementById('comment'+c+'_replyForm').style.display = 'none';
}	

function trackComment(){
   var s 	= s_gi('cmpglobalvista'); 
   s.eVar24	= s.prop7 +" | "+ s.prop4;  
   s.events	= 'event8';
   s.tl(this,'o','Article Comment Posted');
}