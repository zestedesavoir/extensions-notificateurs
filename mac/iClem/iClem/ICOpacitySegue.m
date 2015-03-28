//
//  ICOpacitySegue.m
//  iClem
//
//  Created by Odric Roux-Paris on 28/03/2015.
//  Copyright (c) 2015 Cirdo. All rights reserved.
//

#import "ICOpacitySegue.h"

@implementation ICOpacitySegue

- (void)animatePresentationOfViewController:(NSViewController *)viewController fromViewController:(NSViewController *)fromViewController{
    viewController.view.wantsLayer = YES;
    viewController.view.alphaValue = 0;
    [fromViewController.view addSubview:viewController.view];
    [NSAnimationContext runAnimationGroup:^(NSAnimationContext *context) {
        context.duration = .5;
        viewController.view.animator.alphaValue = 1;
    } completionHandler:nil];
}

- (void)animateDismissalOfViewController:(NSViewController *)viewController fromViewController:(NSViewController *)fromViewController{
    viewController.view.wantsLayer = YES;
    [NSAnimationContext runAnimationGroup:^(NSAnimationContext *context) {
        context.duration = 0.5;
        viewController.view.animator.alphaValue = 0;
    } completionHandler:^{
        [viewController.view removeFromSuperview];
    }];
}

@end
