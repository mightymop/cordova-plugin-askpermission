var exec = require('cordova/exec');
var PLUGIN_NAME = 'Askpermission';

var askpermission = {

	request : function (params,success, error ) {
		exec(success, error, 'Askpermission', 'request', [params]);
	}
};

module.exports = askpermission;
