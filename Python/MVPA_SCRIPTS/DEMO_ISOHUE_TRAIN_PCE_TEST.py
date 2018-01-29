#!/usr/bin/env python
"""
A simple example pymvpa script
"""

# lazy import
from mvpa2.suite import *

# enter input paths
TRAIN_PATH  = os.path.join('training_datapath_here')
TEST_PATH   = os.path.join('test_datapath_here')
MASK_PATH   = os.path.join('mask_datapath_here')

# enter the file names
TRAIN_DATA  = 'training_set_filename_here.nii.gz'
TEST_DATA   = 'testing_set_filename_here.nii.gz'
TRAIN_LABELS= 'label_filename_here.txt'
TEST_LABELS = 'label_filename_here.txt'
MASK_FILE   = 'mask_filename_here.nii.gz'

# select a classifier & partitioner
CLASSIFIER = LinearCSVMC()
PARTITIONER = 'Pre-partitioned dataset'

# source of class targets and chunks definitions
trainAttr   = SampleAttributes(os.path.join(TRAIN_PATH, TRAIN_LABELS))
testAttr    = SampleAttributes(os.path.join(TEST_PATH, TEST_LABELS))

# load data
ds_train   = fmri_dataset(samples = os.path.join(TRAIN_PATH, TRAIN_DATA),
                      targets=trainAttr.targets,
                      chunks=trainAttr.chunks,
                      mask=os.path.join(MASK_PATH, MASK_FILE))

ds_test    = fmri_dataset(samples = os.path.join(TEST_PATH, TEST_DATA),
                      targets=testAttr.targets,
                      chunks=testAttr.chunks,
                      mask=os.path.join(MASK_PATH, MASK_FILE))

# pre-process (no z scoring until I figure out correct way to do it in test set[PCE experiment])
poly_detrend(ds_train, polyord=2, chunks_attr='chunks')
##zscore(ds_train, param_est=('targets', ['rest'])) 

poly_detrend(ds_test , polyord=2, chunks_attr='chunks')
##zscore(ds_test , param_est=('targets', ['rest']))

# delete the unwanted data parts using the names from the labels files
ds_train    = ds_train[ds_train.sa.targets != 'rest']
ds_train    = ds_train[ds_train.sa.targets != 'gray']
ds_train    = ds_train[ds_train.sa.targets != 'discard']

ds_test     = ds_test[ds_test.sa.targets != 'red_rest']
ds_test     = ds_test[ds_test.sa.targets != 'blue_rest']
ds_test     = ds_test[ds_test.sa.targets != 'discard']

# some notes before the results
print 'Mask file:', MASK_FILE
print 'Training labels file:', TRAIN_LABELS
print 'Testing labels file:', TEST_LABELS
print 'Classifier:', CLASSIFIER
print 'Partitioner:', PARTITIONER
print 'Number of features:', ds.nfeatures

# dealing with the classifier
CLASSIFIER.train(ds_train)
est = CLASSIFIER.predict(ds_test)

# getting the error
print 'Mean mismatch error:'
print mean_mismatch_error(est, ds_test.targets)

# full confusion matrix
cm = ConfusionMatrix(targets=ds_test.targets, predictions=est)
print cm
