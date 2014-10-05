//
//  ZDSUpdate.h
//  ZDSNotification
//
//  Created by Odric Roux-Paris on 05/10/2014.
//  Copyright (c) 2014 Cirdo. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface ZDSUpdate : NSObject{
    NSString *raison;
    NSString *version;
}

-(void)update;

@end
