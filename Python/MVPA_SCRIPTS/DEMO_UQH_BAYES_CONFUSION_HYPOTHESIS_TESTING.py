#!/usr/bin/env python
"""
An example script with bayesian confusion hypothesis testing.
"""

# lazy import
from mvpa2.suite import *

# enter input paths
DATA_PATH	= os.path.join('data_path_here')
MASK_PATH	= os.path.join('mask_datapath_here')

# enter the file names
DATA 		= 'dataset_filename_here.nii.gz'
DATA_LABELS = 'label_filename_here.txt'
MASK_FILE   = 'mask_filename_here.nii.gz'

# select a classifier & partitioner
CLASSIFIER = LinearCSVMC()
PARTITIONER = NFoldPartitioner()

# source of class targets and chunks definitions
attr = SampleAttributes(os.path.join(DATA_PATH, DATA_LABELS))

# load data
ds = fmri_dataset(samples = os.path.join(DATA_PATH, DATA),
    targets=attr.targets,
    chunks=attr.chunks,
    mask=os.path.join(DATA_PATH, MASK_FILE))

# pre-process
poly_detrend(ds, polyord=1, chunks_attr='chunks')
zscore(ds, chunks_attr='chunks', param_est=('targets', ['rest']), dtype='float32')

# delete the unwanted data parts using the names from the labels files
ds = ds[ds.sa.targets != 'rest']
ds = ds[ds.sa.targets != 'discard']

# cross validation with bayes confusion hypothesis testing(!mean_mismatch_error in errorfx 
# causes an error! I dont know why.)
cv = CrossValidation(CLASSIFIER, PARTITIONER,
                     errorfx=None, 
                     postproc=ChainNode((Confusion(labels=ds.UT), 
                                         BayesConfusionHypothesis(postprob=True, log=False))))
										 
# run
cv_results = cv(ds)

# prints looks messy for now. (TODO: Better prints also with default classification confusion matrix)
print cv_results.fa.stat
['log(p(C|H))' 'log(p(H|C))']
print "Most likely hypothesis:"
print cv_results.sa.hypothesis[np.argsort(cv_results.samples[:,1])[-1]]

# raw Results:

print "Most likely hypotheses in reverse order:"
print cv_results.sa.hypothesis[np.argsort(cv_results.samples[:,1])[:]]
print "Initial hypotheses:"
print cv_results.sa.hypothesis[:]
print "Probabilities of the initial hypotheses:"
print cv_results.samples

# Bayes Factor Calculations:
# calculate bayes factor versus null using bayesian likelihoods
# Try to falsify H1
Hbest= cv_results.samples[np.argsort(cv_results.samples[:,0])[-1]][0] #first most likely hypothesis
Hsec = cv_results.samples[np.argsort(cv_results.samples[:,0])[-2]][0] #second most likely hypothesis
H1 = cv_results.samples[:,0][-1]    #[['BL'], ['GR'], ['RE'], ['YE']]
H0 = cv_results.samples[:,0][0]     #[['BL', 'GR', 'RE', 'YE']]

print "H0/H1: " ; print H0/H1
print "Hbest/H1:";print Hbest/H1
print "Hsec/H1" ; print Hsec/H1
print "H1/H0: " ; print H1/H0
print "H1/Hbest:";print H1/Hbest
print "H1/Hsec"  ;print H1/Hsec
