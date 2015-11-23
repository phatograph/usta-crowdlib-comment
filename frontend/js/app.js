$(function() {
  $.ajax({
    url: "http://localhost:9998/items",

    success: function( response ) {
      console.log(response);
    },

    error: function( response ) {
      console.log('b');
    }
  });
});
