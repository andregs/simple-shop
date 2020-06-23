// if run UI via WSL2 and you want to proxy api requests to a service running in windows:
// you need to get server's IP in host (windows):
// cat /etc/resolv.conf
// https://docs.microsoft.com/en-us/windows/wsl/compare-versions#accessing-windows-networking-apps-from-linux-host-ip
// but currently the proxy only works if we disable windows firewall for public network
// TODO see https://github.com/microsoft/WSL/issues/4619

const { execSync } = require('child_process');

function getWslHostIp() {
  const command = 'cat /etc/resolv.conf | grep nameserver | sed -e "s/\s*nameserver \s*//g"';
  return execSync(command).toString().trim();
};

module.exports = {
  "/api": {
    target: `http://${getWslHostIp()}:8080`,
    secure: false,
    logLevel: "debug",
    pathRewrite: {
      "^/api": "",
    },
  },
}
