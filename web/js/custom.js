jQuery(document).ready(function(){
    'use strict';
    $(window).load(function(){
        $(".page-loader").fadeOut();});
   
    $('.navbar a.dropdown-toggle').on('click',function(e){
        var elmnt=$(this).parent().parent();
        if(!elmnt.hasClass('nav')){
            var li=$(this).parent();
            var heightParent=parseInt(elmnt.css('height').replace('px',''))/ 2;
var widthParent=parseInt(elmnt.css('width').replace('px',''))- 10;
if(!li.hasClass('open')){
    li.addClass('open');
}
else{
    li.removeClass('open');
    $(this).next().css('top',heightParent+'px');
    $(this).next().css('left',widthParent+'px');
}
return false;
}
});
if($('.navbar').width()>1007)
{$('.nav .dropdown').hover(function(){$(this).addClass('open');},function(){$(this).removeClass('open');});}
$('.scrolling  a[href*="#"]').on('click',function(e){e.preventDefault();e.stopPropagation();var target=$(this).attr('href');$(target).velocity('scroll',{duration:800,offset:-150,easing:'easeOutExpo',mobileHA:false});});$('.scrolling').click(function(){$('html, body').animate({scrollTop:$('#categories').offset().top-50,scrollTop:$('#message').offset().top-50},600);});(function(){$('#thubmnailSlider').carousel({interval:3000});}());(function(){$('.thumbnailCarousel .item').each(function(){var itemToClone=$(this);var i=1;if($(window).width()<=767){for(i=1;i<1;i++){itemToClone=itemToClone.next();if(!itemToClone.length){itemToClone=$(this).siblings(':first');}
itemToClone.children(':first-child').clone().addClass('cloneditem-'+(i)).appendTo($(this));}}else if($(window).width()<=991){for(i=1;i<2;i++){itemToClone=itemToClone.next();if(!itemToClone.length){itemToClone=$(this).siblings(':first');}
itemToClone.children(':first-child').clone().addClass('cloneditem-'+(i)).appendTo($(this));}}else{for(i=1;i<3;i++){itemToClone=itemToClone.next();if(!itemToClone.length){itemToClone=$(this).siblings(':first');}
itemToClone.children(':first-child').clone().addClass('cloneditem-'+(i)).appendTo($(this));}}});}());(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){(i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)})(window,document,'script','https://www.google-analytics.com/analytics.js','ga');ga('create','UA-71155940-7','auto');ga('send','pageview');(function(w,i,d,g,e,t,s){w[d]=w[d]||[];t=i.createElement(g);t.async=1;t.src=e;s=i.getElementsByTagName(g)[0];s.parentNode.insertBefore(t,s);})(window,document,'_gscq','script','//widgets.getsitecontrol.com/46851/script.js');$('.counter').counterUp({delay:10,time:2000});$('.datepicker').datepicker({startDate:'dateToday',autoclose:true});$(document).on('click','.browse',function(){var file=$(this).parent().parent().parent().find('.file');file.trigger('click');});
var $heroSlider=$('.main-slider .inner');if($heroSlider.length>0){$heroSlider.each(function(){var loop=$(this).parent().data('loop'),autoplay=$(this).parent().data('autoplay'),interval=$(this).parent().data('interval')||3000;$(this).owlCarousel({items:1,loop:loop,margin:0,nav:true,dots:true,navText:[],autoplay:autoplay,autoplayTimeout:interval,autoplayHoverPause:true,smartSpeed:700,rtl:false});});}
var owl=$('.owl-carousel.partnersLogoSlider');owl.owlCarousel({loop:true,margin:28,autoplay:true,autoplayTimeout:6000,autoplayHoverPause:true,nav:true,dots:false,smartSpeed:500,rtl:false,responsive:{320:{slideBy:1,items:1},768:{slideBy:1,items:3},992:{slideBy:1,items:4}}});$('a.group').fancybox({'transitionIn':'elastic','transitionOut':'elastic','speedIn':600,'speedOut':200,'overlayShow':false});$('.close-btn').click(function(){$(this).parent().hide();});});