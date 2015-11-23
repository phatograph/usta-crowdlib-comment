$(function() {
  $.ajax({
    url: "http://localhost:9998/items",
    jsonp: "callback",
    dataType: "jsonp",
    data: {
    },

    success: function( response ) {
      console.log('a');
    },

    error: function( response ) {
      console.log('b');
    }
  });
});
