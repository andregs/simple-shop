// if run UI via WSL2 and you want to proxy api requests to a service running in windows:
// you need to get server's IP in host (windows):
// cat /etc/resolv.conf
// https://docs.microsoft.com/en-us/windows/wsl/compare-versions#accessing-windows-networking-apps-from-linux-host-ip
// but currently the proxy only works if we disable windows firewall for public network
// see https://github.com/microsoft/WSL/issues/4619

module.exports = {
  /*"/api": {
    "target": "http://172.19.0.1:8080",
    "secure": false,
    "logLevel": "debug",
    "pathRewrite": {
      "^/api": ""
    },
  },
  "/login": {
    "target": "http://172.19.0.1:8080",
    "secure": false,
    "logLevel": "debug",
  },
  "/logout": {
    "target": "http://172.19.0.1:8080",
    "secure": false,
    "logLevel": "debug",
  },*/
}

// if you run both UI and your backend in linux WSL2:
// module.exports = {
//   "/api": {
//     "target": "http://localhost:8080",
//     "secure": false,
//     "logLevel": "debug",
//     "pathRewrite": {
//       "^/api": ""
//     }
//   }
// }
