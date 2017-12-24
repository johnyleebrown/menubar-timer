var menubar = require('menubar')
const path = require('path')

const mb = menubar({
  resizable: false,
  preloadWindow: true
})

mb.on('ready', () => {
  console.log('app is ready')
})