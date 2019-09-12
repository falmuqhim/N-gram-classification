# N-gram-classification
Fahad Almuqhim, Lecturer
College of Computer and Information Sciences, the Department of Computer Science, Al Imam Mohammad Ibn Saud Islamic University in Riyadh, Saudi Arabia

Bader Alharbi, co-devloper 
-Department of Biostatistics and Bioinformatics, King Abdullah International Medical Research Center/King Saud bin Abdulaziz University for Health Sciences, Riyadh, KSA.
-School of Systems Biology, George Mason University,10900 University Blvd Manassas, VA 20110



N-gram-classification
This project is to reduce the given sequences of amino acid. It takes the clusters, and the letters along with the sequence file, and the desired output directory. Then, it parses the given clusters' file to assign a letter for each cluster, and generate N-gram combinations for further mathematical computations. After that, the N-gram algorithm is used to estimate the probability from relative frequency counts. 


DistinctSequence:
This program is to remove any duplicatation in the given sequences. Also, it disregard sequences that are not within a given sizes.

New feature has been added:
You can now choose an advance settings. By this choice, you can specify the way of reading  N gram from the sequence. You can add as many as you want from disregard and carry chunks For example, take the first 2 letters, then disregard one letter, and then take another 1 letter, and disregard 2 letters, and at the end take 1 letter. 

New feature has been added:
You can extract more sequence from a given sequence file and a length. Then the program will generate sequences of the given length from each sequence in the given file.


New feature has been added:
You can give the program positive and negative files, then the program will shuffle the sequences in the two files randomly in one list. Then, the program will generate positive file, and negative file from the shuffled sequences list. The size of each type will remain.
You can also provide one file to be shuffled to change the order of the sequences in the file.


To run the project from the command line, go to the dist folder in any project and
type the following:

For N-gram-classification:
java -jar "N-gram-classification.jar"

For DistinctSequence:
java -jar "N-gram-classification.jar"
