//
//  ConnectonParse.m
//  ZDSNotification
//
//  Created by Odric Roux-Paris on 28/08/2014.
//  Copyright (c) 2014 Cirdo. All rights reserved.
//

#import "ConnectonParse.h"
#import "HTMLParser.h"
#import "HTMLNode.h"
@implementation ConnectonParse

- (id) init{
    self = [super init];
    if (self){
        NSURL *tutorialsUrl = [NSURL URLWithString:@"http://zestedesavoir.com"];
        
        NSData *tutorialsHtmlData = [NSData dataWithContentsOfURL:tutorialsUrl];
        NSError *error = nil;
        parser = [[HTMLParser alloc] initWithData:tutorialsHtmlData error:&error]; //On parse
    }
    return self;
}


- (BOOL) isConnextionZDS{
    
    HTMLNode *bodyNode = [parser body];
    NSArray *nodes = [bodyNode findChildTags:@"div"]; //On cherche tout les blocs <li>
    
    for (HTMLNode *nodeHTML in nodes ){
        if ([[nodeHTML getAttributeNamed:@"class"] isEqualToString:@"logbox header-right unlogged"]){
            return YES;
        }
        
             
        
    }
    
    return NO;
}

@end
