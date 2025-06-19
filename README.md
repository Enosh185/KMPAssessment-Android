# Assignment - CBC Senior Mobile Developer
This is a take-home assignment which will help us get a better understanding of how you approach a cross-platform development task. 
We will look at your architectural decisions, algorithmic design, coding style and overall problem solving skills.

## Project
A private GitHub repository has been created for you with the shell of a KMP project already set up. We would like you to complete the 
implementation of a KMP module that provides the business logic to fetch content information from the supplied endpoint. You will need 
to complete the implementation of both an Android and iOS app that use this module as well. The base structure needed to complete all 
3 pieces is present in the repository.

### Part One: The Compose Multiplatform (CMP) module
The repository has the base structure for a module named "shared". This module will be responsible for fetching news story data from 
the given endpoint. This module must be usable by both iOS and Android native mobile applications. Each item listed is required for the 
assessment to be considered complete.
- Story data is read from this endpoint: https://cbcmusic.github.io/assessment-tmp/data/data.json.
- Potential networking issues must be handled and managed.
- Data is managed with an appropriate and efficient model.
- The user is able to filter the story items based on headline text.
- The data loaded persists if the app goes offline.
- Include a minimum of 3 unit tests.

### Part Two: The Native Apps
The repository already has the base structure set up for a native Android and iOS application. Complete the implementation of each of these 
so that it makes use of the module you have created in Part One. You can style the UI as you like. Each item listed is required for the 
assessment to be considered complete:

- The story information is fetched via the module created in Part One and displayed in a list.
- Each story item has an image, headline and published date.
- The apps include UI that enables the user to filter the stories as allowed by the module created in Part One.
- If the app goes offline, the story data previously loaded by the module created in Part One is displayed to the user.

### Part Three: The Documentation

Create documentation that could be used to onboard a developer who is new to your project. There is currently a README.md document containing 
the assessment outline that you are welcome to overwrite. You have been sent a PDF version of the assessment requirements as well so 
you should still have that information. For your README, highlight the architectural choices you made and why, any other important 
technical insights and any instructions needed to build your project. 

## Important Rules:
- *You and you alone should complete your submission.*
- *Refrain from using any official CBC branding in your application.*
- *Do not share your work with anyone outside of CBC.*

## Submission
If you have any questions or concerns about the requirements, rules or submission, please reach out to our Talent Acquisition Specialist ***Marium Qadir***
(marium.qadir@cbc.ca) and the hiring Senior Engineering Manager ***Kristen Elliott*** (kristen.elliott@cbc.ca).
