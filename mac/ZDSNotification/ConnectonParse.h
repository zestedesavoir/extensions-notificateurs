//
//  ConnectonParse.h
//  ZDSNotification
//
//  Created by Odric Roux-Paris on 28/08/2014.
//  Copyright (c) 2014 Cirdo. All rights reserved.
//

#import <Foundation/Foundation.h>
@class HTMLParser;

@interface ConnectonParse : NSObject{
    NSURL *tutorialsUrl = [NSURL URLWithString:@"http://zestedesavoir.com"];
    
    HTMLParser *parser;
    
}

-(BOOL)isConnextionZDS;
-(NSString *)getPseudosZDS;
-(NSString *)getUrlImagePseudos;

@end
