#!/usr/bin/env python
"""
An example script with training only permutation test used in our study.
This script is similar to the MC example script in PyMVPA documentation.
"""

# lazy import
from mvpa2.suite import *

# enable progress output for monte carlo estimation
if __debug__:
    debug.active += ["STATMC"]

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

# number of permutations
N_PERM = 10

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

# how often do we want to shuffle the data
repeater = Repeater(count=N_PERM)

# permute the training part of a dataset exactly once
permutator = AttributePermutator('targets', limit={'partitions': 1}, count=1)

# cross validation with null-distribution estimation that permutes the training data for
# each fold independently
null_cv = CrossValidation(
            CLASSIFIER,
            ChainNode([PARTITIONER, permutator], space=PARTITIONER.get_space()),
            errorfx=mean_mismatch_error)

# monte carlo distribution estimator
distr_est = MCNullDist(repeater, tail='left', measure=null_cv,
                       enable_ca=['dist_samples'])
					   
# actual cross validation with null distribution estimation
cv = CrossValidation(CLASSIFIER, PARTITIONER, errorfx=mean_mismatch_error,
                     null_dist=distr_est, enable_ca=['stats'])

# plot related
def plot_cv_results(cv, err, title):
    #make new figure
    pl.figure()
    #indicate colors per each fold (ERROR WARNING: Nr of folds must not be higher than Nr of colors)
    colors = ['red', 'orange', 'green', 'blue', 'purple', 'gray'] 
    #null distribution samples
    dist_samples = np.asarray(cv.null_dist.ca.dist_samples)
    for i in range(len(err)):
        #histogram of all computed errors from permuted data per cross validation fold
        pl.hist(np.ravel(dist_samples[i]), bins=20, color=colors[i],
                label='CV-fold %i' %i, alpha=0.25,
                range=(dist_samples.min(), dist_samples.max()))
        #empirical error
        pl.axvline(np.asscalar(err[i]), color=colors[i])
    #scale x-axis to full range of possible error values    
    pl.xlim(0,1) 
    pl.xlabel(title)

# some notes before the results
print 'Mask file:', MASK_FILE
print 'Data labels file:', DATA_LABELS
print 'Classifier:', CLASSIFIER
print 'Partitioner:', PARTITIONER
print 'Number of features:', ds.nfeatures
print 'Number of permutations:', N_PERM
	
# run
err = cv(ds)

# cross validation errors and corresponding p values
print 'Cross validation errors  :', np.ravel(err)
p = cv.ca.null_prob
print 'Corresponding p-values   :', np.ravel(p)

# print pymvpa confusion matrix
print cv.ca.stats.as_string(description=False)

# plot
plot_cv_results(cv, err, 'Per CV-fold classification error (only training permutation)')

