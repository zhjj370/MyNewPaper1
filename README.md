

# Introduction

> Written at the beginning: My research direction is manufacturing system. I am not a professional software engineer, so my programming code may not be easy to read. If this troubles you, I am very sorry.



* This is a simulation program written in JAVA. The version of jdk is 1.8.0_221.

* This simulation program is related to the paper "A Practical Self-Organizing Operation Mode Considering Global Performance for Manufacturing System based on Auxiliary Monitoring and Rule Modification".

* Since the paper is not published, the principle will not be described in detail for now. This article only briefly introduces how to use it.

  

# Instructions for use

You can define the factory environment and order information for simulation in the resource file. As shown in figure 1, We have defined a set of files, and the data in the files is also the data I used in the paper. 

![resources.png](picture for readme/resources.png)

<center> figure 1</center>

You can configure the processing equipment in the factory according to the following format. 

```
<!DOCTYPE source [
        <!ELEMENT source (machineType+) >
        <!ELEMENT machineType (machineName,bufferAmount,amount,ability+,energyConsumption,life) >
        <!ELEMENT machineName (#PCDATA) >
        <!ELEMENT bufferAmount (#PCDATA) >
        <!ELEMENT amount (#PCDATA) >
        <!ELEMENT ability (#PCDATA) >
        <!ELEMENT energyConsumption (#PCDATA) >
        <!ELEMENT life (#PCDATA) >

        <!ATTLIST machineType id CDATA #REQUIRED>
        <!ATTLIST ability aid CDATA #REQUIRED>
        ]>
<source>
```

You can configure the orders to be placed during the simulation process in the following format. By adjusting the delivery date and order time, you can simulate urgent orders.

```
<!DOCTYPE source [
        <!ELEMENT source (machineType+) >
        <!ELEMENT machineType (machineName,bufferAmount,amount,ability+,energyConsumption,life) >
        <!ELEMENT machineName (#PCDATA) >
        <!ELEMENT bufferAmount (#PCDATA) >
        <!ELEMENT amount (#PCDATA) >
        <!ELEMENT ability (#PCDATA) >
        <!ELEMENT energyConsumption (#PCDATA) >
        <!ELEMENT life (#PCDATA) >

        <!ATTLIST machineType id CDATA #REQUIRED>
        <!ATTLIST ability aid CDATA #REQUIRED>
        ]>
<source>
```

-------

The file `const.java` located under `org.zhjj370.basic` can be used to adjust the simulation mode and define some parameters, as shown in figure 2.  The more important ones are the two parameters `whetherToUseRandom` and `whetherToUseAMRM` shown in the figure. Adjusting them can reproduce the simulation in my paper.

* `whetherToUseRandom` is used to determine whether to change the processing time you define. In order to get the same result in my article, please set `whetherToUseRandom` to false
* `whetherToUseAMRM` is used to determine whether to enable AMRM mode.

![const.png](picture for readme/const.png)

<center>figure 2</center>

------

`AdjustmentCoefficients.java` defines the parameters that need to be adjusted in the AMRM mode in this simulation. We gave a detailed description in the fourth chapter of the paper.



![adjustment](picture for readme\adjustment.png)

<center>figure 3</center>

------

Under the `org.zhjj370.rule` package, you can define your own self-organizing rules and use these rules in the program.

* The "machines select parts" rule defined in the paper is `CompareMaCapacity1.java` in figure 4.
* The "parts select machins" rule defined in the paper is `CompareWaitingPart1.java` in figure 4.

![rule](picture for readme\rule.png)

<center>figure 4</center>

-------

The `simulation.java` file located in the `org.zhjj370` package is the main program entry for this simulation, as shown in figure 5.

![rule](picture for readme\simulation1.png)

<center>figure 5</center>

If your operating system has specified the default opening program for the `.md` file, the `out.md` file located in the `output` folder will be opened after the simulation ends, and it will output the main simulation results. You can also find vector images in `.pdf` format that can be used for writing latex in the `output\pdf` file.

It should be noted that the simulation program is a single simulation, not all the results in my paper can be obtained at one time.

* For example, if you want to see the effect of AMRM mode, you can set `whetherToUseAMRM` to `true`. You can view the coefficients I defined in the `simulation.java` file, as shown in figure 5. (See the paper for detailed design ideas).

* For example, if you want to see the effect of non-AMRM mode, you can set `whetherToUseAMRM` to `false`. You can define fixed coefficients in the `AdjustmentCoefficients.java` file, as shown in figure 3. (See the paper for detailed design ideas).









