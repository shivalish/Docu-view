/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./src/**/*.{js,jsx,ts,tsx}",],
  theme: {
    extend: {    
      colors: {
      'iso-blue': '#21303A',
      'iso-white': '#FFFFFF',
      'iso-blue-grey': {
        100: '#4A5B63',
        200: '#6E7B82'
      },
      'iso-grey': '#d9d9d9'
      }
    },
  },
  plugins: [
    require('@headlessui/tailwindcss')
  ],
}

