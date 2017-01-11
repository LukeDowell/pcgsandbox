function displayNativeAd( adObj ){
 var nativeadpage = adObj.page + ""
 if (nativeadpage == "landing" ) {
    var isHomepageObj = document.querySelector(".content_box_middle > .two_col");
    if ( isHomepageObj !== null) {
       setTimeout(function(){  displayLandingPageNativeAd( adObj )   }, 1000);
    } else {
       setTimeout(function(){  displaySectionLandingPageNativeAd( adObj )   }, 1000);
    }
 } else {
       setTimeout(function(){   displayArticleNativeAd( adObj )   }, 1000);
 }
}

function displayLandingPageNativeAd( adObj ){
 var innerNativeContentThumbanil = "";
 if ( adObj.youtube_vid != "") {
	innerNativeContentThumbanil = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/" + adObj.youtube_vid + "?rel=0&amp;showinfo=0\" frameborder=\"0\" allowfullscreen><\/iframe>";
 } else {
	innerNativeContentThumbanil = "<a target=\"_blank\" href=\"" + adObj.clickurl +  "\"><img width=\"108\" class=\"img-thumb thumbnail_right\" src=\"" + adObj.img + "\"></a>";
 }
var innerNativeHeader = "<div class=\"nativelpad-header\">" + unescape(adObj.headerlabel) + "</div>";
var innerNativeContent = "<span class=\"story_title\"><a href=\"" + adObj.clickurl + "\">" + unescape(adObj.title) + "</a></span><div class=\"clear\"></div>";
innerNativeContent += innerNativeContentThumbanil   + unescape( adObj.description) ;
var innerNativeBottom  = "<div style=\"height:1px;\"></div><div class=\"nativelpad-sponsor\">" + unescape(adObj.sponsorlabel) +"</div><hr style=\"padding-bottom:0\">";

var nativeInsertHeaderObjs = document.querySelectorAll(".content_box_middle > .two_col > .comment_icon");
var nativeInsertObj = nativeInsertHeaderObjs[4];
var nativeInsertObjContent = document.createElement('div');
nativeInsertObjContent.className = "nativelpad-content";
nativeInsertObjContent.innerHTML = innerNativeHeader + innerNativeContent + innerNativeBottom; 
nativeInsertObj.parentNode.insertBefore(nativeInsertObjContent,  nativeInsertObj);
}

function displaySectionLandingPageNativeAd( adObj ){
 var innerNativeContentThumbanil = "";
 if ( adObj.youtube_vid != "") {
	innerNativeContentThumbanil = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/" + adObj.youtube_vid + "?rel=0&amp;showinfo=0\" frameborder=\"0\" allowfullscreen><\/iframe>";
 } else {
	innerNativeContentThumbanil = "<a target=\"_blank\" href=\"" + adObj.clickurl +  "\"><img width=\"108\" class=\"img-thumb thumbnail_left\" src=\"" + adObj.img + "\"></a>";
 }
var innerNativeHeader = "<div class=\"feed_item\"><div class=\"nativeslpad-header\">" + unescape(adObj.headerlabel) + "</div>";
var innerNativeContent = innerNativeContentThumbanil + "<span><span class=\"story_title\"><a href=\"" + adObj.clickurl + "\">" + unescape(adObj.title) + "</a></span>";
innerNativeContent += "<br>"  + unescape( adObj.description) ;
var innerNativeBottom  = "<div style=\"height:1px;\"><div class=\"nativeslpad-sponsor\">" + unescape(adObj.sponsorlabel) +"</div></div><hr style=\"padding-bottom:0\">";

var nativeInsertHeaderObjs = document.querySelectorAll(".content_bg .feed_item");
var nativeInsertObj = nativeInsertHeaderObjs[4];
var nativeInsertObjContent = document.createElement('div');
nativeInsertObjContent.className = "nativeslpad-content";
nativeInsertObjContent.innerHTML = innerNativeHeader + innerNativeContent + innerNativeBottom; 
nativeInsertObj.parentNode.insertBefore(nativeInsertObjContent,  nativeInsertObj);
}



function displayArticleNativeAd( adObj ){
 var innerNativeContentThumbanil = "";
 if ( adObj.youtube_vid != "") {
	innerNativeContentThumbanil = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/" + adObj.youtube_vid + "?rel=0&amp;showinfo=0\" frameborder=\"0\" allowfullscreen><\/iframe>";
 } else {
	innerNativeContentThumbanil = "<a  target=\"_blank\" href=\"" + adObj.clickurl +  "\"><img src=\"" + adObj.img + "\"></a>";
 }

 var innerNativeHeader = "";
 var innerNativeContent = "<div class=\"nativeapad-content\"><div class=\"nativeapad-thumbnail\">";
 innerNativeContent += innerNativeContentThumbanil;
 
 innerNativeContent += "</div>";
 innerNativeContent +="<div><span class=\"nativeapad-header\">" + unescape(adObj.headerlabel) + "</span></div>";
 innerNativeContent += "<div class=\"nativeapad-post\"><a target=\"_blank\" href=\"" + adObj.clickurl +  "\">" + unescape(adObj.title) + "</a>";
 innerNativeContent += "<p>" + unescape( adObj.description) + "</p></div><div style=\"height:5px;\"></div><span class=\"nativeapad-sponsor\">" + unescape(adObj.sponsorlabel) + "</span>";
 innerNativeContent += "<div class=\"nativeapad-reset\"></div></div>";
 var innerNativeBottom  = "</div>";
 
 var nativeInsertObjContent = document.createElement('div');
 nativeInsertObjContent.innerHTML = innerNativeHeader + innerNativeContent + innerNativeBottom; 
 
 var nativeInsertObj;
 nativeInsertObj = document.querySelector("#dynamiccomments");
 /*
 comment out the feature to insert the native within article content.   Native ad will be placed at the bottom
 var nativeInsertPs = document.querySelectorAll(".item_body p");   //Blog and article have different template, don't use .item_body > p
 var nativeArticleMain = document.querySelector(".item_body");
 var nativeInsertPsLength = nativeInsertPs.length;
 if (  nativeInsertPsLength > 15){
    nativeInsertObj = nativeInsertPs[ 13 ];
    nativeInsertObj.parentNode.insertBefore(nativeInsertObjContent,  nativeInsertObj);
 } else if(  nativeInsertPsLength > 10){
    nativeInsertObj = nativeInsertPs[ 8 ];
    nativeInsertObj.parentNode.insertBefore(nativeInsertObjContent,  nativeInsertObj);
 } else {
    nativeInsertObj = nativeArticleMain;
    nativeInsertObj.appendChild(nativeInsertObjContent);
 }
 */
 nativeInsertObj.parentNode.insertBefore(nativeInsertObjContent,  nativeInsertObj);
}