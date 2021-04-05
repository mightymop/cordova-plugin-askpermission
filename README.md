# Android:

### 1. Add plugin
cordova plugin add https://github.com/mightymop/cordova-plugin-askpermission.git
### 2. For Typescript add following code to main ts file: 
/// &lt;reference types="cordova-plugin-askpermission" /&gt;<br/>
### 3. Usage:
```
window.askpermission.request(params,succescallback,errorcallback);

params= { permissions : string[]}
	
```
