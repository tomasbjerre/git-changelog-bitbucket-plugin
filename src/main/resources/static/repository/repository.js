(function ($) {
 'use strict';
 var config_resource = AJS.contextPath() + "/rest/changelog/1.0";
 $(document).ready(function() {
  console.log("Using rest resource at: "+config_resource);

  function getParam(name){
   var results = new RegExp('[\?&]' + name + '=([^&#]*)').exec(window.location.hash);
   if (results == null){
    return null;
   } else {
    return results[1] || 0;
   }
  }

  function getFrom() {
   return $("#changelog-from").val();
  }

  function getTo() {
   return $("#changelog-to").val();
  }

  function getEndpoint(from,to) {
   var endpoint = config_resource;
   
   endpoint = endpoint + "/" + $("#changelog-project").val();
   endpoint = endpoint + "/" + $("#changelog-repository").val();

   if (!from && !to) {
    return endpoint;
   }
   
   if (from === '0000000000000000000000000000000000000000') {
    endpoint = endpoint + "/fromcommit/" + from;
   } else {
    endpoint = endpoint + "/fromref/" + from;
   }

   endpoint = endpoint + "/toref/" + to;

   return endpoint;
  }

  function updateChangelog(endpoint,from,to) {
   $.ajax({
    url: endpoint,
    dataType: "json",
    type: "GET",
    error: function(xhr, data, error) {
     console.log(xhr);
     console.log(data);
     console.log(error);
     $(".changelog").html(error);
    },
    success: function(data, text, xhr) {
     $("#changelog-content").html(data.changelog);

     $("#changelog-from option").remove();
     $("#changelog-from").append('<option value="0000000000000000000000000000000000000000">First commit</option>');
     for (var i = 0; i < data.references.length; i++) {
      var selected = from === data.references[i] ? "selected" : "";
      $("#changelog-from").append('<option value="'+data.references[i]+'" '+selected+'>'+data.references[i]+'</option>');
     }

     $("#changelog-to option").remove();
     for (var i = 0; i < data.references.length; i++) {
      var selected = to === data.references[i] ? "selected" : "";
      $("#changelog-to").append('<option value="'+data.references[i]+'" '+selected+'>'+data.references[i]+'</option>');
     }
    }
   });
  }

  function getChangelog(from,to) {
   var endpoint = getEndpoint(from,to);
   updateChangelog(endpoint,from,to);
  }
  
  function setHash() {
    var from = getFrom();
    var to = getTo();
 	window.location.hash = "?from="+from+"&to="+to;
  }
  
  function hashChanged() {
   var from = getParam("from");
   var to = getParam("to");
   getChangelog(from,to);
  }
  
  $('#changelog-from').on('change', function() {
   setHash();
   hashChanged();
  });
  
  $('#changelog-to').on('change', function() {
   setHash();
   hashChanged();
  });
  
  if (window.location.hash.length > 0) {
   hashChanged();
  } else {
   getChangelog(null,null);
  }
 });
})(AJS.$ || jQuery);