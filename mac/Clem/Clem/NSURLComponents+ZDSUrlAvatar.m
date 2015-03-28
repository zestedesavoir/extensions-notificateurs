//
//  NSURLComponents+ZDSUrlAvatar.m
//  Clem
//
//  Created by Odric Roux-Paris on 14/03/2015.
//  Copyright (c) 2015 Cirdo. All rights reserved.
//

#import "NSURLComponents+ZDSUrlAvatar.h"

@implementation NSURLComponents (ZDSUrlAvatar)

- (void)ZDSrectifiUrlAvatar{
    NSString *urlString = [self string];
    NSRange rangeUrl = [urlString rangeOfString:@"http"];
    
    if (rangeUrl.length == 0){
        self.host = @"zestedesavoir.com";
        self.scheme = @"https";
    }
}

@end
