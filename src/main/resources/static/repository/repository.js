(function ($) {
 'use strict';
 var config_resource = AJS.contextPath() + "/rest/changelog/1.0";
 var changelogRaw;
 
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

  function getTo() {
   return $("#changelog-to").val();
  }

  function getEndpoint(from,to) {
   var endpoint = config_resource;
   
   endpoint = endpoint + "/" + $("#changelog-project").val();
   endpoint = endpoint + "/" + $("#changelog-repository").val();

   if (!from || !to) {
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
   $('.git-changelog-spinner').spin();
   $('.git-changelog-window').prop('disabled',true);
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
     changelogRaw = data.changelog;
     $("#changelog-content").html(changelogRaw);

     $("#changelog-from branches").remove();
     $("#changelog-from").append('<li><a data-branch="0000000000000000000000000000000000000000" href="javascript:void(0);">First commit</a></li>');
     for (var i = 0; i < data.references.length; i++) {
      $("#changelog-from").append('<li><a data-branch="'+data.references[i]+'" href="javascript:void(0);">'+data.references[i]+'</a></li>');
     }
     $("#changelog-from a").click(function() {
      setHash('from',$(this).data('branch'));
     });

     $("#changelog-to branches").remove();
     for (var i = 0; i < data.references.length; i++) {
      $("#changelog-to").append('<li><a data-branch="'+data.references[i]+'" href="javascript:void(0);">'+data.references[i]+'</a></li>');
     }
     $("#changelog-to a").click(function() {
      setHash('to',$(this).data('branch'));
     });
    },
    error: function(data, text, xhr) {
     $('#git-changelog-plugin-status').html('Error :(');
    },
    complete: function() {
     $('.git-changelog-spinner').spinStop();
     $('.git-changelog-window').prop('disabled',false);
    }
   });
  }

  function getChangelog(from,to) {
   setStatus(from,to);
   var endpoint = getEndpoint(from,to);
   updateChangelog(endpoint,from,to);
  }
  
  function setHash(withParam,withValue) {
   var from = getParam('from');
   if (withParam == 'from') {
    from = withValue;
   }

   var to = getParam('to');
   if (withParam == 'to') {
    to = withValue;
   }

   window.location.hash = "?from="+from+"&to="+to;
   getChangelog(from,to);
  }

  function setStatus(from,to) {
   var status = 'From '+from+' to '+to+'.';
   if (!from && !to) {
    status = 'Select from and to branch.';
   } else if (!from) {
    status = 'Select from branch.';
   } else if (!to) {
    status = 'Select to branch.';
   }
   $('#git-changelog-plugin-status').html(status);
  }

  if (window.location.hash.length > 0) {
   var from = getParam("from");
   var to = getParam("to");
   getChangelog(from,to);
  } else {
   getChangelog(null,null);
  }
  
  $('.git-changelog-window').click(function() {
   window.open().document.write(changelogRaw);
  });
 });
})(AJS.$ || jQuery);