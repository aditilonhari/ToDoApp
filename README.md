# Pre-work - To-do App

To-do App is an android app that allows building a todo list and basic todo items management functionality including adding new items, editing and deleting an existing item.

Submitted by: Aditi Lonhari

Time spent: 15-20 hours spent in total

## User Stories

The following **required** functionality is completed:

* Required: User can **successfully add and remove items** from the todo list
* Requried: User can **tap a todo item in the list and bring up an edit screen for the todo item** and then have any changes to the text reflected in the todo list.
* Required: User can **persist todo items** and retrieve them properly on app restart

The following **optional** features are implemented:

* Optional: Tweak the style improving the UI / UX, play with colors, images or backgrounds
* Suggested: Persist the todo items into SugarORM(SQLite) instead of a text file
* Suggested: Improve style of the todo items in the list using a custom adapter(Array Adapter)
* Suggested: Add support for completion due dates for todo items (and display within listview item)
* Suggested: Use a DialogFragment instead of new Activity for editing items
* Optional: Add support for selecting the status of each todo item (and display in listview item)
* Optional: Added Custom Toast with an image and text
* Optional: Added two listviews to separate list items depending on the status
* Optional: Custom the button to round shape

## Video Walkthrough 

Here's a walkthrough of implemented user stories:

<img src='https://github.com/aditilonhari/ToDoApp/blob/master/ToDoApp_final.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

## Notes

This was my first time creating an Android app and working on Android studio. It was a great experience although, I faced a few challenges while working on this. I was getting an error on creating the ArrayList using todo.txt file. I tried to debug it and find solution for it online, when ultimately one of my friends, who is an android developer, suggested to create a local file and push it to device internal memory. It worked fine and later on I could figure the error I was facing. Also, setting up styles on the various Widgets was a bit different coming from html-css world. Various subdirectories in "values" folder were used for styling.

Extended Features:
I worked on additional features after submitting the initial app. 
I have added all of the suggested features such as replacing the text file with a SQLite database. I used the simpler version, SugarORM, which is a wrapper API for the SQL queries and database creation. I created a custom array-adapter to hold the complex data model which consisted of a Task object holding a task name, task due date and task status. I also tried my hands at various dialog fragments such as Toasts and AlertDialog. I learned a lot about creating complex apps while working on the extended features and I feel that I am more confident and eager to try and create more android apps.

## License

    Copyright 2016 Aditi Lonhari

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
