import { render, screen, cleanup} from '@testing-library/react';
const { Builder, By, until } = require('selenium-webdriver');
const chai = require('chai');
const chaiAsPromised = require('chai-as-promised');
const fs = require('fs');


chai.use(chaiAsPromised);

describe('Test Suite', () => {
    jest.setTimeout(1800000);
    
    let driver;

    beforeEach(async () => {
        driver = await new Builder().forBrowser('chrome').build();
        await driver.get('http://localhost:3000');
    });

    afterEach(async () => {
        await driver.quit();
    });

    it('basic login', async () => {
        
        await new Promise(resolve => setTimeout(resolve, 1000));
        //const pageSource = await driver.getPageSource();
        //fs.writeFile('output.html', pageSource, (err) => {
        //    if (err) throw err;
        //    console.log('The file has been saved!');
        //});
        const usernameField = await driver.findElement(By.xpath("//input[@type='text' and @class='h-10 w-full px-5 bg-iso-grey rounded-md' and @value='']"));
        await usernameField.sendKeys('Pablo');
        const passwordField = await driver.findElement(By.xpath("//input[@type='password' and @class='h-10 w-full px-5 bg-iso-grey rounded-md' and @value='']"));
        await passwordField.sendKeys('password');
        await new Promise(resolve => setTimeout(resolve, 1000));
        const button = await driver.findElement(By.className('bg-iso-blue-grey-100 font-bold p-2 text-white rounded-md transition-all duration-300 ease-in-out hover:scale-110 hover:bg-iso-blue-grey-200 w-32 h-10'));
        await button.click();
        await new Promise(resolve => setTimeout(resolve, 1000));
        const checkboxes = await driver.findElements(By.xpath("//input[@type='checkbox' and @class='form-checkbox h-5 w-5 mr-4 ml-4']"));
        for (let checkbox of checkboxes.slice(0, 3)) {
            await checkbox.click();
        }
        const downloadbutton = await driver.findElement(By.xpath("//button[text()='Download' and @type='button' and @class='bg-iso-blue-grey-100 font-bold p-2 text-white rounded-md transition-all duration-300 ease-in-out hover:scale-110 hover:bg-iso-blue-grey-200']"));
        downloadbutton.click();
        //const pageSource2 = await driver.getPageSource();
        //fs.writeFile('output2.html', pageSource2, (err) => {
        //    if (err) throw err;
        //    console.log('The file has been saved!');
        //});
        await new Promise(resolve => setTimeout(resolve, 60000));
    });
});