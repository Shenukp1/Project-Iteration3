# SENG 300: Project Iteration 3

The third and final iteration is the most complex yet.  Your team is now big and simply organizing and managing it will become a task in itself.

## Authors
- [Andy Tang: 10139121](https://github.com/Thisisme-Andrew)
- [Ayman Inayatali Momin: 30192494](https://github.com/aymanmomin)
- [Darpal Patel: ](https://github.com/darpalp)
- [Dylan Dizon: ](https://github.com/dylanrylee)
- [Ellen Bowie: 30191922](https://github.com/ebeeze1)
- [Ishita Udasi: 30170034](https://github.com/ishita-udasi)
- [Jason Very: ](https://github.com/jvery11)
- [Jesse Leinan: 00335214](https://github.com/JesseL382)
- [Kear Sang heng: 30087289](https://github.com/kearsang)
- [Khadeeja Abbas: 30180776](https://github.com/KhadeejaAbbas)
- [Kian Sieppert: 30134666](https://github.com/givenn19)
- [Michelle Le: 30145965](https://github.com/michelle-le1)
- [Sean Gilluley: ](https://github.com/HQCPlays)
- [Shenuk Perera: 30086618](https://github.com/shenukp1)
- [Sina Salahshour: ](https://github.com/SinSalahshour)
- [Tristan Van Decker: 30160634](https://github.com/Muffletruffle)
- [Usharab Khan: ](https://github.com/usharabkhan)
- [Simrat Virk: 30000516](https://github.com/SimratV0)
- [Raja Muhammed Omar: 30159575](https://github.com/raja-omar)
- [: ](https://github.com/)

## Revision History
- Version 4: The previous hardware revision was incomplete; I have adjusted `AttendantStation` to permit `ISelfCheckoutStation` instances to be used.

- Version 3: The hardware has been revised to make the `ISelfCheckoutStation` interface more useful without having to cast. Specifically, the public fields in `AbstractSelfCheckoutStation` have been changed to private and a corresponding getter method has been added for each; those getter methods have been added to the interface as well, as a result.

- Version 2: The hardware has been revised to allow `AttendantStation` to refer to arbitrary combinations of   ISelfCheckoutStation   instances.

- Version 1: Initial version.


## Requirements
(1) You must work with this team on this project iteration.

(2) You must communicate with your team in a timely manner to organize and collaborate. Failure to do so will cause you to be penalized, regardless of the performance of the team as a whole, up to and including obtaining an F.  You have been warned.  Be responsible.

(3) You and your team must develop a portion of the control software for the self-checkout system, atop the hardware simulation provided to you in the attached ZIP file as <V4: changed>com.thelocalmarketplace.hardware_0.3.3</V4>. You must not alter the source code therein.

(4) You and your team must extend the control software for the self-checkout system to support the remaining use cases, according to the organization’s use case model (v3.0), which may have undergone changes since you last looked at it.

This project iteration is cumulative: you must continue to provide support for the use cases mentioned in earlier iterations.  If the details of the requirements have changed, you need to make the necessary adjustments.

(5) You and your team must develop a graphical user interface for the self-checkout station and for the attendant station, using the Java Swing library.

(6) You may extend any of the implementations that your teammates worked on in the second iteration, or any combination thereof; it is for the team to decide which one is the best option to proceed with.

(7) You and your team must develop an automated test suite for testing your application. Logical test case selection and coverage both matter.  GUIs must be interacted with programmatically to test them (i.e., via the API provided by the GUI framework).

(8) You and your team must begin every source file in the control software and its test suite with a comment that contains your names and UCID numbers.

(9) You and your team may provide a supplementary, one (1) page explanation of how your application works.

(10) You and your team must provide a Git log that demonstrates who performed commits over time. (This can be used partially as evidence regarding individual opinions; see below.)

(11) You and your team must provide a detailed structure diagram to explain how your application works; in this situation, what is important is to be complete in terms of the types, packages, and relationships between all of these.

## Organization

There are multiple aspects of the work to be considered here:

- design;
- implementation;
- automated testing, bug reports, bug repairs;
- documenting;
- managing and tracking;
- demonstrating.

All of these aspects will require effort and are equally important. The same person can perform multiple roles, or roles can switch as necessary. I expect different people to be more interested in some of these than in others. It is your collective job to figure out how to organize. (In a real organization, you would be hired into a job with a detailed description and/or you would be assigned to tasks meeting your skill set, but we don't have the luxury to simulate that.) All information about all these tasks should be recorded in files and committed to the group's Git repository.

It is PARAMOUNT that you and your team decide on who will do which of these tasks (or more specific ones, like implementing a certain use case). To do that, you need to detail immediately what all those tasks are, and then be prepared to change that list when you understand the details better.  Above all else, realize the need to be flexible and make adjustments when unexpected circumstances arise.

The job of the design/implementation team(s) should be familiar to you now.

The job of the management team will be to maintain/update the list of tasks, the assignment to individuals (done by consensus or by someone being the boss), and the completion status. It is important to have a fallback position if your team doesn't manage to complete everything by the deadline. It is also important that we and you all know who was supposed to do what and who actually did (or didn't do) what.

The job of the quality assurance team will be to write automated test cases, run them, write bug reports, and assign them to people who are most appropriate to repair them.

The documentation team needs to create the required models. There are potentially a lot of these. What details are key? Which can be abstracted away? Try to explain the reality with its strengths and weaknesses. It is unrealistic to expect that the design you came up with is fantastic: aim for "good enough" and recognize what is good and bad about it.

The work of the demonstration team will be: to demonstrate that the use cases for the system are supported (or that they were not completed). The presentation will be performed LIVE on the last day of classes, for no more than 10 minutes.  You will be asked to sign up for a slot a few days after the start of this iteration.

## Factors affecting the grade
- Factors affecting your grade
- Completeness of the functionality implemented
- Quality of the automated test suite implemented
- Coverage (instruction and branch) of your functionality by your test suite
- Adherence to properties related to "clean code"
- Adherence to the six design properties discussed in lecture 
- Conformance to the requirements specified in these instructions
- Adherence to general properties of good engineering practice (i.e., it will not be acceptable for you to come up with some "clever scheme" to "get around" other requirements: don't be a lawyer)
- Lateness
- Individual contribution
- Submission of your peer/self evaluation
- Adherence to general properties of academic integrity and the Collaboration and Plagiarism Policy of this course
- The demonstration will be graded separately, as per the course outline

## Solution Submission
Relative to the above description, you are required to submit:

1. the com.thelocalmarketplace.software project detailed above, in a ZIP file
2. the com.thelocalmarketplace.software.test project detailed above, in a ZIP file
3. an optional one page of written textual explanations, with the cover page
4. the Git log for your team's Git repository


In your **submission comments**, you are required to provide:

You are required to submit:

your diagrams (plus a cover page with the names and student numbers of your teammates, plus an optional explanation page) as a single PDF document;
a ZIP file containing your exported Eclipse project com.thelocalmarketplace.software that contains your extended control software;
a ZIP file containing your exported Eclipse project com.thelocalmarketplace.software.test that contains your automated test suite; and,
your Git log file.    
    
You must also each provide Individual Performance Evaluations regarding the contributions of yourself and your teammates, under the separate survey instrument. 

By default, everyone on the team will receive the same grade; individuals who fail to contribute will be penalized.  Be prepared to offer evidence to support your claims.


## Considerations and Advice

The design of your software matters in this iteration (remember: this does not refer to your diagrams; the design is a set of properties related to the structure of the implementation, which one can sometimes judge by looking at appropriate diagrams).  While you cannot maximize all design factors simultaneously, you can maximize most of them simultaneously.

Note that you and your teammates will be making use of the structure diagram on the final examination, so take it seriously.

The user interface design matters for completeness of functionality and for the quality of your demonstration.

When deadlines loom, you are most likely to make a dumb mistake.  (Hint: This is equally true for the hardware team!)

Remember: In a good team, everyone contributes, but not necessarily in the same way.  Software development is not only about implementation.

Do NOT get your teammates to commit YOUR work. Don’t be naïve: you will have no evidence that you did the work.  We are not interested in seeing that you sent a couple of posts in some Discord chat; that’s not real evidence!

You will need GUIs for each of a set of CustomerStations and an AttendantStation.  I would suggest that you consider providing separate GUIs to simulate customer input, attendant input, and provide a means of showing what change is returned and what is printed on the receipt.
