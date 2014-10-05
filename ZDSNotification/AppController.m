//
//  AppController.m
//  ZDSNotification
//
//  Created by Odric Roux-Paris on 28/08/2014.
//  Copyright (c) 2014 Cirdo. All rights reserved.
//

#import "AppController.h"
#import  "PreferenceController.h"
NSString  * const CSlider = @"CSlider";
NSString  * const CNotificationPreferenceImage = @"CNotificationPreferenceImage";
NSString * const CVersion = @"CVersion";


@implementation AppController


-(IBAction)showPreferencePanel:(id)sender{
    
    if (!preferenceController) {
		preferenceController = [[PreferenceController alloc] init];
	}
		[preferenceController showWindow:self];
    
}

+(void)initialize{
    NSMutableDictionary *defaultValue = [NSMutableDictionary dictionary];
    BOOL i = YES;
    [defaultValue setObject:[NSNumber numberWithFloat:5.0] forKey:CSlider];
    [defaultValue setObject: [NSNumber numberWithBool:i] forKey:CNotificationPreferenceImage];
    [defaultValue setObject:@"1.0.0" forKey:CVersion];
    
    [[NSUserDefaults standardUserDefaults] registerDefaults:defaultValue];
   
}

@end
