(function ($) {
 'use strict';
 var config_resource = AJS.contextPath() + "/rest/changelog/1.0/config";
 $(document).ready(function() {
  $('input[name="save"]').click(function(e) {
   var $form = $(this).closest('form');
   $(".post",$form).html("Saving...");
   $.ajax({
    url: config_resource,
    dataType: "json",
    type: "POST",
    contentType: "application/json",
    data: JSON.stringify($form.serializeArray(), null, 2),
    processData: false,
    error: function(xhr, data, error) {
     $(".error."+xhr.responseJSON.field,$form).html(xhr.responseJSON.error);
     if (xhr.responseJSON.field) {
      $(".post",$form).html("There were errors, form not saved!");
     } else { 
      $(".post",$form).html(xhr.responseText);
     }
    },
    success: function(data, text, xhr) {
     $(".post",$form).html('');
     getAll();
    }
   });
  });

  function getAll() {
   $.ajax({
    url: config_resource,
    dataType: "json"
   }).done(function(config) {
    $.each(config, function(fieldIndex,field_map) {
     var safe_value = field_map.value.replace(/[^a-zA-Z\_]/g,'');
     $('input[type="text"][name="'+field_map.name+'"]').attr('value', field_map.value);
     $('textarea[name="'+field_map.name+'"]').text(field_map.value);
     $('input[type="hidden"][name="'+field_map.name+'"]').attr('value', field_map.value);
     $('input[type="checkbox"][name="'+field_map.name+'"][value="'+safe_value+'"]').attr('checked','checked');
    });
   });
  }
  
  getAll();
 });
})(AJS.$ || jQuery);