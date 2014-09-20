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

    
    HTMLParser *parser;
    
}

-(BOOL)isConnextionZDS;
-(NSString *)getPseudosZDS;
-(NSString *)getUrlImagePseudos;
- (NSString *) parseString:(NSString *)string; //Parse le string et en revoie un notre

@end
