//
//  Created by Guilherme Rambo on 16/11/14.
//  Copyright (c) 2014 Guilherme Rambo. All rights reserved.
//

#import "ICPushSegue.h"
#import "iClem-Bridging-Header.h"


@import QuartzCore;

@interface ICPushAnimator : NSObject <NSViewControllerPresentationAnimator>
@end

@implementation ICPushAnimator

#define kPushAnimationDuration 0.3f

- (void)animatePresentationOfViewController:(NSViewController *)viewController fromViewController:(NSViewController *)fromViewController

{
    [fromViewController viewDidDisappear];
    
    viewController.view.frame = NSMakeRect(NSWidth(fromViewController.view.frame), // x
                                           0, // y
                                           NSWidth(fromViewController.view.frame), // width
                                           NSHeight(fromViewController.view.frame)); // height
    viewController.view.autoresizingMask = NSViewWidthSizable|NSViewHeightSizable;
    
    [fromViewController.view addSubview:viewController.view];
    
    NSRect destinationRect = fromViewController.view.frame;
   
    
    [NSAnimationContext runAnimationGroup:^(NSAnimationContext *context) {
        context.duration = kPushAnimationDuration;
        context.timingFunction = [CAMediaTimingFunction functionWithName:kCAMediaTimingFunctionEaseOut];
        
        [viewController.view.animator setFrame:destinationRect];
    } completionHandler:nil];
}

- (void)animateDismissalOfViewController:(NSViewController *)viewController fromViewController:(NSViewController *)fromViewController
{
    NSRect destinationRect = NSMakeRect(NSWidth(fromViewController.view.frame), // x
                                        0, // y
                                        NSWidth(fromViewController.view.frame), // width
                                        NSHeight(fromViewController.view.frame)); // height
    [fromViewController viewDidAppear];
    
    [NSAnimationContext runAnimationGroup:^(NSAnimationContext *context) {
        context.duration = kPushAnimationDuration;
        context.timingFunction = [CAMediaTimingFunction functionWithName:kCAMediaTimingFunctionEaseIn];
        
        [viewController.view.animator setFrame:destinationRect];
    } completionHandler:^{
        [viewController.view removeFromSuperview];
    }];
}

@end

@implementation ICPushSegue

- (void)perform
{
    [self.sourceController presentViewController:self.destinationController animator:[[ICPushAnimator alloc] init]];
}

@end
