(function ($) {
 'use strict';
 var config_resource = AJS.contextPath() + "/rest/changelog/1.0/rest";
 $(document).ready(function() {
  function getAll() {
   console.log(config_resource);
  }
  
  getAll();
 });
})(AJS.$ || jQuery);